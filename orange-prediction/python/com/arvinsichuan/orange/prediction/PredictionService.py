# Ref from https://github.com/Dref360/tuto_keras_web


import os
from multiprocessing import Lock
from multiprocessing.managers import BaseManager

import numpy as np
from keras.models import model_from_json
from keras.preprocessing import image
from matplotlib import pyplot

from python.com.arvinsichuan.orange.preprocess.DataPreprocessLite import DataPreProcess as DPP
from python.opencv.plate_localization import detectPlateRough


class Prediction(object):
    def __init__(self, json_model=None, weights=None, t=7):
        print("==========Init Multiprocess Locks=======")
        self.__mutex__ = Lock()
        self.__model__ = None
        self.__json_model__ = None
        self.__weights__ = None
        self.__processor__ = DPP(t)

    def initialize(self):
        print("==========Loading Model===========")
        if not self.__json_model__:
            self.__json_model__ = os.path.join("D:", "model_ite19-0.json")
        if not self.__weights__:
            self.__weights__ = os.path.join("D:", "model-weights-2018-04-23-13-44-15.h5")

        with open(self.__json_model__, 'r') as f:
            self.__json_model__ = f.read()
        self.__model__ = model_from_json(self.__json_model__)
        self.__model__.load_weights(self.__weights__)
        self.__model__.predict(np.random.randn(1, 200, 500, 3))
        print("==========Loading Model END===========")

    def detect_one(self, img):
        temp = detectPlateRough(img.astype(np.uint8))
        if len(temp) == 0:
            return None
        results = []
        for t in temp:
            img_to_pred = image.array_to_img(t[0])
            img_to_pred = self.__processor__.reshape_image(img_to_pred, target_size=(200, 500, 3))
            img_to_pred = image.img_to_array(img_to_pred) / 255
            results.append(self.detect_characters(img_to_pred))
        return results

    def detect_characters(self, img_to_pred):
        with self.__mutex__:
            preds = self.__model__.predict(np.array([img_to_pred]))
            ints_label = np.squeeze(np.argmax(preds, axis=-1))
            return "".join(self.__processor__.int_to_string(ints_label))


class KerasManager(BaseManager):
    pass


KerasManager.register('Prediction', Prediction)

if __name__ == '__main__':
    with KerasManager() as manager:
        print('Main', os.getpid())
        kerasmodel = manager.Prediction()
        kerasmodel.initialize()
        img = image.load_img("C://Users/75744/git/Orange/dataset/云AU7526/云AU7526.jpg")
        img = image.img_to_array(img)
        print(kerasmodel.detect_one(img))
        img = image.load_img("C://Users/75744/git/Orange/dataset/云AU7526/云AU7526.jpg")
        img = image.img_to_array(img)
        print(kerasmodel.detect_one(img))
