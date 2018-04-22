

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

export class BasicInfo {
  private _copyrightOwner: string;
  private _copyrightOwnerLink: string;
  private _info: string;
  private _yearFrom: number;


  constructor(copyrightOwner: string, copyrightOwnerLink: string, info: string, yearFrom: number) {
    this._copyrightOwner = copyrightOwner;
    this._copyrightOwnerLink = copyrightOwnerLink;
    this._info = info;
    this._yearFrom = yearFrom;
  }

  get copyrightOwner(): string {
    return this._copyrightOwner;
  }

  set copyrightOwner(value: string) {
    this._copyrightOwner = value;
  }

  get copyrightOwnerLink(): string {
    return this._copyrightOwnerLink;
  }

  set copyrightOwnerLink(value: string) {
    this._copyrightOwnerLink = value;
  }

  get info(): string {
    return this._info;
  }

  set info(value: string) {
    this._info = value;
  }

  get yearFrom(): number {
    return this._yearFrom;
  }

  set yearFrom(value: number) {
    this._yearFrom = value;
  }
}
