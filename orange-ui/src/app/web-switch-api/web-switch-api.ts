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

export class WebSwitchApi<T> {
  private _code: string;
  private _message: string;
  private _info: string;
  private _data: T;
  private _status: Status;


  get status(): Status {
    return this._status;
  }

  set status(value: Status) {
    this._status = value;
  }

  get code(): string {
    return this._code;
  }

  set code(value: string) {
    this._code = value;
  }

  get message(): string {
    return this._message;
  }

  set message(value: string) {
    this._message = value;
  }

  get info(): string {
    return this._info;
  }

  set info(value: string) {
    this._info = value;
  }

  get data(): T {
    return this._data;
  }

  set data(value: T) {
    this._data = value;
  }


}

export enum Status {
  /**
   * OK - Status OK
   * Status code starts with 0 (0[0-9]*)
   */
  OK,
  /*
   *  EXCEPTION - Has Exception or have Exceptions
   *  Error code starts with prefix 1 (1[0-9]*)
   */
  EXCEPTION,
  /**
   * non of data found Error code 101
   */
  EMPTY_DATA,
  /*
   *   Duplicated data found which can not be.
   *   Error code 102
   */
  DUPLICATE_DATA
}
