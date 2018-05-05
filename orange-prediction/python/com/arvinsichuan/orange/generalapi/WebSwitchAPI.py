STATUS_AND_CODE = {0: "OK", 1: "EXCEPTION", 2: "EMPTY_DATA", 3: "DUPLICATE_DATA", "OK": 0,
                   "EXCEPTION": 1, "EMPTY_DATA": 3, "DUPLICATED_DATA": 4}


def get_status_value(status_name):
    status_name = status_name.upper()
    index = STATUS_AND_CODE[status_name]
    return STATUS_AND_CODE[index]


class WebSwitchAPI(object):

    def __init__(self, status=None):
        if not status:
            self.__WEB_ENTITY__ = {"status": get_status_value("OK"), "code": STATUS_AND_CODE["OK"], "message": None,
                                   "data": None, "info": None}
        else:
            self.__WEB_ENTITY__ = {"status": get_status_value(status), "code": STATUS_AND_CODE[status], "message": None,
                                   "data": None, "info": None}

    def is_ok(self):
        self.__WEB_ENTITY__["status"] = get_status_value("OK")
        self.__WEB_ENTITY__["code"] = STATUS_AND_CODE["OK"]
        return self

    def have_exception(self, e, message=None):
        self.__WEB_ENTITY__["status"] = get_status_value("EXCEPTION")
        self.__WEB_ENTITY__["code"] = STATUS_AND_CODE["EXCEPTION"]
        if message:
            self.__WEB_ENTITY__["message"] = str(e) + ", Additional Message:{}".format(message)
        else:
            self.__WEB_ENTITY__["message"] = e
        return self

    def add_data(self, data):
        self.__WEB_ENTITY__["data"] = data
        return self

    def get_data(self):
        return self.__WEB_ENTITY__["data"]

    def add_info(self, info):
        self.__WEB_ENTITY__["info"] = info
        return self

    def empty_data(self, msg=None):
        self.have_exception("EMPTY DATA", msg)
        return self

    def duplicated_data(self, msg):
        self.have_exception("DUPLICATED DATA", msg)
        return self

    def get_entity(self):
        return self.__WEB_ENTITY__
