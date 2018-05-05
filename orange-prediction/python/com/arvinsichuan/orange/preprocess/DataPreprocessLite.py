import numpy as np
from keras.utils import to_categorical

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


class DataPreProcess(object):

    def __init__(self, t=9):

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
        self.__T = t

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

    def onehot_encoding(self, label_dataset, t_label=None):
        '''
            Input:

                label_dataset - list for label_datasets respect to the image data list.
                vocabulary_to_index - vocabulary for the words in the label.
            Output:
                Loh - One-hot encoded Label.

        '''
        if not t_label:
            t_label = self.__T
        # L = Label
        # convert string sequence to int arrays
        L = np.array([
            self.string_to_int(l, t_label) for l in label_dataset
        ])
        label_oh = np.array(list(map(lambda x: to_categorical(x, num_classes=len(self.__vocabulary_to_index)), L)))
        return np.array(label_oh > 0, dtype=np.bool)

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
