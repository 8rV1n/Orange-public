
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
import {BasicInfo} from '../beans/BasicInfo';
import {Observable} from 'rxjs/Observable';
import {catchError} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {log} from 'util';
import {WebApiService} from '../../base-services/web-api.service';

@Injectable()
export class BasicInfoService extends WebApiService {

  constructor(private http: HttpClient) {
    super();
  }

  public getBasicInfo(): Observable<BasicInfo> {
    const url = BasicInfoService.API_URL + 'copyright/';
    log('Fetching...');
    log(url);
    const observable: Observable<BasicInfo> = this.http.get<BasicInfo>(url).pipe(
      catchError(this.handleError('getHeroes', BasicInfo.prototype))
    );
    log('Fetching End');
    return observable;
  }

}
