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
import {HttpClient, HttpParams} from '@angular/common/http';
import {User} from './user';
import {Observable} from 'rxjs/Observable';
import {WebSwitchApi} from '../web-switch-api/web-switch-api';
import {LogLevel, LogService} from '../base-services/log.service';
import {catchError} from 'rxjs/operators';
import {BasicInfoService} from '../footer/services/basic-info.service';
import {CookieService} from 'ngx-cookie';
import {Router} from '@angular/router';


const ENDPOINT = 'auth/';
const ENDPOINT_LOGIN = ENDPOINT + 'login';
const ENDPOINT_STATUS = ENDPOINT + 'status';
const ENDPOINT_LOGOUT = ENDPOINT + 'logout';
const ENDPOINT_REGISTER = 'users/signUp';

@Injectable()
export class AuthenticationService extends WebApiService {


  private static _user: User;

  public static checkAuth(): boolean {
    let status = false;
    if (AuthenticationService._user != null) {
      status = true;
    }
    return status;
  }

  constructor(private http: HttpClient,
              private cookiesSevice: CookieService,
              private router: Router) {
    super();
  }

  public register(user: User): Observable<WebSwitchApi<User>> {
    const url = BasicInfoService.API_URL + ENDPOINT_REGISTER;
    LogService.logDev(LogLevel.INFO, 'Submitting...');
    LogService.logDev(LogLevel.INFO, url);
    const observable: Observable<WebSwitchApi<User>> =
      this.http.post<WebSwitchApi<User>>(url, user)
        .pipe(catchError(this.handleError<WebSwitchApi<User>>('register')));
    LogService.logDev(LogLevel.INFO, 'Submitting End');
    return observable;
  }

  public login(user: User): Observable<WebSwitchApi<{}>> {
    const url = AuthenticationService.API_URL + ENDPOINT_LOGIN;
    const params = new HttpParams()
      .set('username', user.username)
      .set('password', user.password);
    LogService.logDev(LogLevel.INFO, 'Login...');
    LogService.logDev(LogLevel.INFO, ENDPOINT_LOGIN);
    const observable: Observable<WebSwitchApi<{}>> = this.http.post<WebSwitchApi<{}>>(url, params).pipe(
      catchError(this.handleError<WebSwitchApi<{}>>('login'))
    );
    LogService.logDev(LogLevel.INFO, 'Login End');
    return observable;
  }

  private logout(user: User): Observable<WebSwitchApi<{}>> {
    const url = AuthenticationService.API_URL + ENDPOINT_LOGOUT;
    LogService.logDev(LogLevel.INFO, 'Logout...');
    LogService.logDev(LogLevel.INFO, ENDPOINT_LOGIN);
    const observable: Observable<WebSwitchApi<{}>> = this.http.post<WebSwitchApi<{}>>(url, null).pipe(
      catchError(this.handleError<WebSwitchApi<{}>>('logout'))
    );
    LogService.logDev(LogLevel.INFO, 'Logout End');
    return observable;
  }

  public doLogout() {
    this.logout(this.user).subscribe(switchApi => {
      if (switchApi) {
        AuthenticationService._user = null;
        this.cookiesSevice.removeAll();
        this.router.navigate(['']);
      }
    });
  }


  public doLogInWithNameAndPwd(username, password) {
    let status = false;
    const user = new User(username, password, true);
    this.login(user).subscribe(switchApi => {
      LogService.logDev(LogLevel.INFO, switchApi);
      if (switchApi) {
        if (Number(switchApi.code) === 0) {
          user.password = '[PROTECTED]';
          AuthenticationService._user = user;
          status = true;
        }
      }
    });
  }

  public doLogIn(user: User, callback) {
    let status = false;
    this.login(user).subscribe(switchApi => {
      LogService.logDev(LogLevel.INFO, switchApi);
      if (switchApi) {
        if (Number(switchApi.code) === 0) {
          user.password = '[PROTECTED]';
          AuthenticationService._user = user;
          status = true;
          callback(status);
        }
      } else {
        callback(null);
      }
    });
  }

  public get user(): User {
    return AuthenticationService._user;
  }
}
