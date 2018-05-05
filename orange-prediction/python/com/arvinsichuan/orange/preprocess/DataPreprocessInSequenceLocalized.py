import os

import h5py as h5
import matplotlib.pyplot as plt
import numpy as np
from keras.preprocessing import image
from keras.utils import to_categorical

from python.opencv.plate_localization import detectPlateRough as detectLocation

try:
    from PIL import ImageEnhance
    from PIL import Image as pil_image
except ImportError:
    pil_image = None

# Resize image from keras ideas
if pil_image is not None:
    _PIL_INTERPOLATION_METHODS = {
        'nearest': pil_image.NEAREST,
        'bilinear': pil_image.BILINEAR,
        'bicubic': pil_image.BICUBIC,
    }
    # These methods were only introduced in version 3.4.0 (2016).
    if hasattr(pil_image, 'HAMMING'):
        _PIL_INTERPOLATION_METHODS['hamming'] = pil_image.HAMMING
    if hasattr(pil_image, 'BOX'):
        _PIL_INTERPOLATION_METHODS['box'] = pil_image.BOX
    # This method is new in version 1.1.3 (2013).
    if hasattr(pil_image, 'LANCZOS'):
        _PIL_INTERPOLATION_METHODS['lanczos'] = pil_image.LANCZOS


class DataPreprocess(object):

    def __init__(self, T=9):

        # Initialize the dictionaries
        vocabulary_list = []
        province_vocab = [
            '京', '津', '冀', '晋', '蒙', '辽', '吉', '黑', '沪', '苏', '浙', '皖', '闽', '赣',
            '鲁', '豫', '鄂', '湘', '粤', '桂', '琼', '川', '贵', '云', '渝', '藏', '陕', '甘',
            '青', '宁', '新'
        ]
        special_vocab = ['使', '警', '领', '学', '挂', '超', '临', '港', '澳', '军', '未', '甲', '海', '空']
        letter_vocab = [
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        ]
        number_vocab = [str(i) for i in range(0, 10)]
        symbol_vocab = ['#']

        # Concat all vocabularies together
        vocabulary_list.extend(province_vocab)
        vocabulary_list.extend(special_vocab)
        vocabulary_list.extend(letter_vocab)
        vocabulary_list.extend(number_vocab)
        vocabulary_list.extend(symbol_vocab)

        # Initialize the vocabulary dictionaries
        self.__vocabulary_to_index = {}
        self.__index_to_vocabulary = {}

        # Update the values for the dictionaries
        for i, item in enumerate(vocabulary_list):
            self.__vocabulary_to_index[item] = i
            self.__index_to_vocabulary[i] = item
        # Initialize the default sequence length T
        self.__T = T

    @property
    def vocabulary_to_index(self):
        return self.__vocabulary_to_index

    @property
    def index_to_vocabulary(self):
        return self.__index_to_vocabulary

    @property
    def sequence_length(self):
        return self.__T

    # keras way of processing size
    def reshape_image(self, img, target_size=None, interpolation='nearest'):
        if target_size is not None:
            width_height_tuple = (target_size[1], target_size[0])
            if img.size != width_height_tuple:
                if interpolation not in _PIL_INTERPOLATION_METHODS:
                    raise ValueError(
                        'Invalid interpolation method {} specified. Supported '
                        'methods are {}'.format(
                            interpolation,
                            ", ".join(_PIL_INTERPOLATION_METHODS.keys())))
                resample = _PIL_INTERPOLATION_METHODS[interpolation]
                img = img.resize(width_height_tuple, resample)
        return img

    def onehot_encoding(self, label_dataset, T_label=None):
        '''
            Input:

                label_dataset - list for label_datasets respect to the image data list.
                vocabulary_to_index - vocabulary for the words in the label.
            Output:
                Loh - One-hot encoded Label.

        '''
        if not T_label:
            T_label = self.__T
        # L = Label
        # convert string sequence to int arrays
        L = np.array([
            self.string_to_int(l, T_label) for l in label_dataset
        ])
        Loh = np.array(list(map(lambda x: to_categorical(x, num_classes=len(self.__vocabulary_to_index)), L)))
        return np.array(Loh > 0, dtype=np.bool)

    def string_to_int(self, string, length=None):
        """
        Converts all strings in the vocabulary into a list of integers representing the positions of the
        input string's characters in the "vocab"

        Arguments:
        string -- input string, e.g. '沪C123456'
        length -- the number of time steps you'd like, determines if the output will be padded or cut
        vocab -- vocabulary, dictionary used to index every character of your "string"

        Returns:
        rep -- list of integers (or '<unk>') (size = length) representing the position of the string's character in the vocabulary
        """
        if not length:
            length = self.__T
        # Make upper to standardize, the plates are all uppercase.
        string = string.upper()

        # Truncating if longer than length
        if len(string) > length:
            string = string[:length]

        rep = list(map(lambda x: self.__vocabulary_to_index.get(x, '#'), string))

        # Padding
        if len(string) < length:
            rep += [self.__vocabulary_to_index['#']] * (length - len(string))

        return rep

    def int_to_string(self, ints):
        """
        Output a machine readable list of characters based on a list of indexes in the machine's vocabulary

        Arguments:
        ints -- list of integers representing indexes in the machine's vocabulary
        inv_vocab -- dictionary mapping machine readable indexes to machine readable characters 

        Returns:
        l -- list of characters corresponding to the indexes of ints thanks to the inv_vocab mapping
        """

        l = [self.__index_to_vocabulary[i] for i in ints]
        return l

    def process_images(self,
                       dataset_path,
                       save_path='.',
                       save_name='processed_dataset.h5',
                       image_output_shape=(720, 1280, 3),
                       image_read_batch_size=32,
                       show_mid_process_img=False,
                       detecting_location=True,
                       max_candidate_num_localize=3,
                       manual_choose=False,
                       compression=None):
        """
        Input:
            dataset_path,
            save_path='.',
            save_name='processed_dataset.h5',
            image_output_shape=(720, 1280, 3)
            image_read_batch_size=32,
            show_mid_process_img=False,
            detecting_location=True,
            max_candidate_num_localize=3,
            manual_choose=False,
            compression=None
        Output:
            file on the specified path(save_path with save_name)
        """

        # Read Vocabulary size from vocabulary
        vocab_size = len(self.__vocabulary_to_index)
        T = self.__T

        # Read label list
        labels_list = [
            x for x in os.listdir(dataset_path)
            if os.path.isdir(os.path.join(dataset_path, x))
        ]

        # Saving path with file
        final_file = os.path.join(save_path, save_name)
        # Creating file
        h5_file = None
        try:
            # Try to create H5 file
            h5_file = h5.File(final_file, 'w-')
        except IOError as e:
            override = input("File exists, override?(Y/N)").lower()
            override = override == 'y' or override == ''
            if override:
                # Override H5 file
                h5_file = h5.File(final_file, 'w')
            else:
                h5_file = None
                print('aborted for not overriding.')
        except OSError as e:
            print("EXCEPTION: %s." % (e))
            print("File may be not accessiable!")
            print("Saving H5 File(%s) Failed in path %s" % (save_name, save_path))
            if h5_file:
                h5_file.close()

        if h5_file:

            # Load images shapes
            height, width, depth = image_output_shape
            basic_images_dataset_shape = (0, height, width, depth)
            max_images_dataset_shape = (None, height, width, depth)

            # Create Dataset for images
            images_dataset = h5_file.create_dataset(
                'images',
                basic_images_dataset_shape,
                dtype=np.uint8,
                chunks=True,
                maxshape=max_images_dataset_shape,
                compression=compression)

            basic_labels_dataset_shape = (0, T, vocab_size)
            max_labels_dataset_shape = (None, T, vocab_size)
            labels_dataset = h5_file.create_dataset(
                'labels',
                basic_labels_dataset_shape,
                dtype=np.bool,
                chunks=True,
                maxshape=max_labels_dataset_shape,
                compression=compression)

            total_num = len(labels_list)

            # Iteration for loading images from files and labeling to one-hot encoding
            for index, label in enumerate(labels_list):
                # Generate path
                cur_path = os.path.join(dataset_path, label)

                # Images caches for current label(TODO, batch size optimization.)
                images_caches = []

                # Processing hint
                print('processing progress: %d / %d ' % (index + 1, total_num))

                # Walk and read the image
                for cur_root, ds, fs in os.walk(cur_path):
                    for file in fs:
                        print('Reading class %s: %s' %
                              (label, os.path.join(cur_path, file)))
                        if detecting_location:
                            img = image.load_img(os.path.join(cur_path, file))
                            img = image.img_to_array(img)

                            print("Localizing croping...")
                            print(img.shape)

                            temp = detectLocation(img.astype(np.uint8))
                            if manual_choose:
                                show_mid_process_img = True
                                max_candidate_num_localize = len(temp)
                            for i in range(len(temp)):

                                if i <= max_candidate_num_localize:
                                    img = temp[i][0]
                                    print(img.shape)

                                    # Process image shape to terget shape
                                    if show_mid_process_img:
                                        plt.imshow(img)
                                        plt.show()
                                    if manual_choose:
                                        manual_choose_y = input("Pass Data existence test?(Y/N)").lower()
                                        manual_choose_y = manual_choose_y == 'y' or manual_choose_y == ''
                                        if not manual_choose_y:
                                            continue

                                    img = image.array_to_img(img)
                                    img = self.reshape_image(img, target_size=image_output_shape)
                                    img = image.img_to_array(img)
                                    assert img.shape == image_output_shape, (
                                        "Image Shape Exception:{path}, shape is {shape} but it should be {expectedShape}".
                                            format(
                                            path=cur_path,
                                            expectedShape=image_output_shape,
                                            shape=str(img.shape)))
                                    images_caches.append(img)
                            if len(temp) == 0:
                                continue
                            print("Localizing croping end...")
                        else:
                            img = image.load_img(os.path.join(cur_path, file), target_size=image_output_shape)
                            img = image.img_to_array(img)

                            assert img.shape == image_output_shape, (
                                "Image Shape Exception:{path}, shape is {shape} but it should be {expectedShape}".
                                    format(
                                    path=cur_path,
                                    expectedShape=image_output_shape,
                                    shape=str(img.shape)))
                            images_caches.append(img)

                        # Batch way to process
                        image_caches_len = len(images_caches)
                        if image_caches_len >= image_read_batch_size:
                            # Append images and labels into file
                            print('%d images loaded in folder %s' %
                                  (image_caches_len, cur_path))
                            if image_caches_len:
                                current_length = len(images_dataset)
                                appended_length = current_length + image_caches_len
                                images_resize_shape = (
                                    appended_length, basic_images_dataset_shape[1],
                                    basic_images_dataset_shape[2],
                                    basic_images_dataset_shape[3])
                                labels_resize_shape = (
                                    appended_length, basic_labels_dataset_shape[1],
                                    basic_labels_dataset_shape[2])
                                images_dataset.resize(images_resize_shape)
                                images_dataset[-image_caches_len:] = images_caches
                                labels_dataset.resize(labels_resize_shape)
                                labels_dataset[
                                -image_caches_len:] = self.onehot_encoding(
                                    [label] * image_caches_len, T)
                            # Clear Cache
                            images_caches = []
                    # Append images and labels into file that are not reach the batch size
                    image_caches_len = len(images_caches)
                    print('%d images loaded in folder %s' % (image_caches_len,
                                                             cur_path))
                    if image_caches_len:
                        current_length = len(images_dataset)
                        appended_length = current_length + image_caches_len
                        images_resize_shape = (appended_length,
                                               basic_images_dataset_shape[1],
                                               basic_images_dataset_shape[2],
                                               basic_images_dataset_shape[3])
                        labels_resize_shape = (appended_length,
                                               basic_labels_dataset_shape[1],
                                               basic_labels_dataset_shape[2])
                        images_dataset.resize(images_resize_shape)
                        images_dataset[-image_caches_len:] = images_caches
                        labels_dataset.resize(labels_resize_shape)
                        labels_dataset[
                        -image_caches_len:] = self.onehot_encoding(
                            [label] * image_caches_len, T)
                    # Clear Cache
                    images_caches = []

        # Close file
        if h5_file:
            print('\n\n###########  Summary  ###########')
            print('%d images(%d classes) and its labels saved in the dataset file(%s)' %
                  (len(images_dataset), len(labels_list), final_file))
            h5_file.close()
            fsize = os.path.getsize(final_file) / float(1024 * 1024)
            print('file size: %.2f MB' % (round(fsize, 2)))
