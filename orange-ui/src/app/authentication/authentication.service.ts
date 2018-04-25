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
import {WebApiService} from '../base-services/web-api.service';
import {HttpClient} from '@angular/common/http';
import {User} from './user';
import {Observable} from 'rxjs/Observable';
import {WebSwitchApi} from '../web-switch-api/web-switch-api';
import {LogLevel, LogService} from '../base-services/log.service';


const ENDPOINT = '/auth/';
const ENDPOINT_LOGIN = ENDPOINT + 'login';
const ENDPOINT_STATUS = ENDPOINT + 'status';
const ENDPOINT_LOGOUT = ENDPOINT + 'logout';
const ENDPOINT_SIGN_UP = ENDPOINT + 'signUp';

@Injectable()
export class AuthenticationService extends WebApiService {


  private _user: User;

  constructor(private http: HttpClient) {
    super();
  }

  private login(user: User): Observable<WebSwitchApi> {
    // let url = AuthenticationService.API_URL + "data/platforms.json";
    LogService.log(LogLevel.INFO, true, 'Fetching...');
    LogService.log(LogLevel.INFO, true, ENDPOINT_LOGIN);
    // let observable: Observable<> = this.http.get<PlatformStatus[]>(url).pipe(
    //   catchError(this.handleError<PlatformStatus[]>('getHeroes'))
    // );
    LogService.log(LogLevel.INFO, true, 'Fetching End');
    // return observable;
    return null;
  }

  private logout(user: User): Observable<WebSwitchApi> {
    return null;
  }

  private validateAuthStatus(user: User): Observable<WebSwitchApi> {
    return null;
  }

  public doLogin(user: User): boolean {
    return false;
  }


  public doValidation(user: User): boolean {
    return false;
  }

  public doLogout(user: User): boolean {
    return false;
  }


  public get user(): User {
    return this._user;
  }
}
