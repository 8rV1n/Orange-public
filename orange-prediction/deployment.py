#!/usr/bin/env python
from tornado.httpserver import HTTPServer
from tornado.ioloop import IOLoop
from tornado.wsgi import WSGIContainer

from python.com.arvinsichuan.orange.webEndpoints import app


def start():
    print("========  Start Server  =========")
    http_server = HTTPServer(WSGIContainer(app))
    http_server.listen(5000)
    IOLoop.instance().start()


start()

if __name__ == '__main__':
    start()
