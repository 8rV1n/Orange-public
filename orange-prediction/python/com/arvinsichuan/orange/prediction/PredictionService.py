# Ref from https://github.com/Dref360/tuto_keras_web


import os
import time
from multiprocessing import Lock
from multiprocessing.managers import BaseManager

import numpy as np
from keras.models import model_from_json
from keras.preprocessing import image

from python.com.arvinsichuan.orange.preprocess.DataPreprocessLite import DataPreProcess as DPP
from python.opencv.plate_localization import detect_plate_rough


class Predictor(object):
    def __init__(self, json_model=None, weights=None, t=7):
        print("==========Init Multiprocess Locks=======")
        self.__mutex = Lock()
        self.__model = None
        self.__json_model = None
        self.__weights = None
        self.__processor = DPP(t)

    def initialize(self):
        print("==========Loading Model===========")
        if not self.__json_model:
            self.__json_model = os.path.join(".", "model_ite19-0.json")
        if not self.__weights:
            self.__weights = os.path.join(".", "model-weights-2018-04-23-13-44-15.h5")

        with open(self.__json_model, 'r') as f:
            self.__json_model = f.read()
        self.__model = model_from_json(self.__json_model)
        self.__model.load_weights(self.__weights)
        self.__model.predict(np.random.randn(1, 200, 500, 3))
        print("==========Loading Model END===========")

    def detect_one(self, img):
        start_time = time.time()
        temp = detect_plate_rough(img.astype(np.uint8))
        print("TIME STATICS: LOCALIZATION --- {} s".format(time.time() - start_time))
        if len(temp) == 0:
            return None
        results = []
        start_time = time.time()
        for t in temp:
            img_to_pred = image.array_to_img(t[0])
            img_to_pred = self.__processor.reshape_image(img_to_pred, target_size=(200, 500, 3))
            img_to_pred = image.img_to_array(img_to_pred) / 255
            results.append(self.detect_characters(img_to_pred))
        print("TIME STATICS: CHAR DETECTION --- {} s".format(time.time() - start_time))
        return results

    def detect_characters(self, img_to_pred):
        with self.__mutex:
            preds = self.__model.predict(np.array([img_to_pred]))
            ints_label = np.squeeze(np.argmax(preds, axis=-1))
            return "".join(self.__processor.int_to_string(ints_label))
