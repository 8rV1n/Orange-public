/*
 *     This project is one of projects of ArvinSiChuan.com.
 *     Copyright (C) 2018, ArvinSiChuan.com <https://arvinsichuan.com>.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';

export const enum LogLevel {
  DEBUG,
  INFO,
  WARN,
  ERROR,
  FATAL,
}

@Injectable()
export class LogService {

  constructor() {
  }


  static logDev(loglevel: LogLevel = LogLevel.INFO, msg: any) {
    this.log(loglevel, true, msg);
  }

  static log(loglevel: LogLevel = LogLevel.INFO, devMode: boolean = false, msg: any) {
    if (devMode) {
      if (!environment.production) {
        this.realLog(loglevel, msg);
      }
    } else {
      this.realLog(loglevel, msg);
    }
  }

  static realLog(logLevel: LogLevel, msg: any) {
    switch (logLevel) {
      case LogLevel.DEBUG:
        console.log(msg);
        break;
      case LogLevel.INFO:
        console.log(msg);
        break;
      case LogLevel.WARN:
        console.warn(msg);
        break;
      case LogLevel.ERROR:
        console.error(msg);
        break;
      case LogLevel.FATAL:
        console.error(msg);
        break;
      default:
        break;
    }
  }


}
