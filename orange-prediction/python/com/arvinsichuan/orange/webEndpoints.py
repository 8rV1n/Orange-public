import time, os

from flask import request, Flask, jsonify
from keras.preprocessing import image

from python.com.arvinsichuan.orange.generalapi.WebSwitchAPI import WebSwitchAPI
# APP config
from python.com.arvinsichuan.orange.prediction.PredictionService import Predictor

app = Flask(__name__)
ALLOWED_EXTENSIONS = ('jpg', 'jpeg')
MODEL_FILE = os.path.join("D:", "model_ite19-0.json")
MODEL_WEIGHTS = os.path.join("D:", "model-weights-2018-04-23-13-44-15.h5")


def init_predictor():
    global predictor
    predictor = Predictor(json_model=MODEL_FILE, weights=MODEL_WEIGHTS)
    predictor.initialize()


def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


@app.route('/detection', methods=['POST'])
def detection():
    time_start = time.time()
    web_entity = WebSwitchAPI("OK")
    if request.method == 'POST' and 'image' in request.files:
        file = request.files['image']
        if allowed_file(file.filename):
            img = image.load_img(file)
            img = image.img_to_array(img)
            results = predictor.detect_one(img)
            if not results:
                web_entity.have_exception("NO PLATE DETECTED")
            else:
                web_entity.is_ok().add_data(results)
        else:
            web_entity.empty_data("Uploaded Data is not image data that is required")
    else:
        web_entity.empty_data("No Image Data Found.")
    time_end = time.time()
    web_entity.add_info("time cost: %.2fs" % (time_end - time_start))
    return jsonify(web_entity.get_entity())


init_predictor()

if __name__ == '__main__':
    init_predictor()
    app.debug = False
    app.run()
