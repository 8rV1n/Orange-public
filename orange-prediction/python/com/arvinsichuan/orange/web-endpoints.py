import os
import time

from flask import request, Flask, jsonify
from flask_uploads import UploadSet, IMAGES, configure_uploads
from keras.preprocessing import image

from python.com.arvinsichuan.orange.generalapi.WebSwitchAPI import WebSwitchAPI
# APP config
from python.com.arvinsichuan.orange.prediction.PredictionService import KerasManager

app = Flask(__name__)
app.config['UPLOADED_PHOTO_DEST'] = os.path.dirname(os.path.abspath(__file__))
app.config['UPLOADED_PHOTO_ALLOW'] = IMAGES

# Upload filter
photos = UploadSet('PHOTO')
configure_uploads(app, photos)

app.config["MODEL_MANAGER"] = KerasManager()
app.config["MODEL_STARTED"] = False


@app.route('/detection', methods=['POST'])
def detection():
    time_start = time.time()
    if not app.config["MODEL_STARTED"]:
        app.config["MODEL_MANAGER"].start()
        app.config["MODEL_STARTED"] = app.config["MODEL_MANAGER"].Prediction()
        app.config["MODEL_STARTED"].initialize()
    web_entity = WebSwitchAPI("OK")
    if request.method == 'POST' and 'image' in request.files:
        file = request.files['image']
        img = image.load_img(file)
        img = image.img_to_array(img)
        results = app.config["MODEL_STARTED"].detect_one(img)
        if not results:
            web_entity.have_exception("NO PLATE DETECTED")
        else:
            web_entity.is_ok().add_data(results)
    else:
        web_entity.empty_data("No Image Data Found.")
    time_end = time.time()
    web_entity.add_info("time cost: %.2fs" % (time_end - time_start))
    return jsonify(web_entity.get_entity())


if __name__ == '__main__':
    app.debug = False
    app.run()
