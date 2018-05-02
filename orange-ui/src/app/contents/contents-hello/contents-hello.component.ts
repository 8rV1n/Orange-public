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

import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../../authentication/authentication.service';
import {NzMessageService} from 'ng-zorro-antd';
import {Router} from '@angular/router';
import {LogLevel, LogService} from '../../base-services/log.service';

@Component({
  selector: 'app-contents-hello',
  templateUrl: './contents-hello.component.html',
  styleUrls: ['./contents-hello.component.css']
})
export class ContentsHelloComponent implements OnInit {
  private _helloMsgBrief = 'Hello';

  private _helloMsg = 'Be an user in the system and continue.';

  logInForm: FormGroup;

  constructor(private fb: FormBuilder,
              private authService: AuthenticationService,
              private msgService: NzMessageService,
              private router: Router) {
    this._createFrom();
  }

  ngOnInit() {

  }

  private _createFrom() {
    this.logInForm = this.fb.group({
      username: [null, [Validators.required]],
      password: [null, [Validators.required]],
      remember: [true],
    });
  }

  _submitForm() {
    if (this.logInForm.controls) {
      for (const i of Object.keys(this.logInForm.controls)) {
        this.logInForm.controls[i].markAsDirty();
      }
      const msgId = this.msgService.loading('Please wait a moment.', {nzDuration: 0}).messageId;
      this.authService.login(this.prepareUser()).subscribe(switchApi => {
        this.msgService.remove(msgId);
        LogService.logDev(LogLevel.INFO, switchApi);
        if (switchApi) {
          if (Number(switchApi.code) === 0) {
            this.msgService.success('Login Success, please login.', {nzDuration: 3000});
            this.router.navigate(['detection']);
          }
        }
      });

    }
  }

  private prepareUser() {
    const formModel = this.logInForm.value;
    return {
      username: formModel.username as string,
      password: formModel.password as string,
      enabled: true,
      authorities: []
    };
  }

  get helloMsgBrief(): string {
    return this._helloMsgBrief;
  }

  get helloMsg(): string {
    return this._helloMsg;
  }


  get userName() {
    return this.logInForm.get('username');
  }

  get password() {
    return this.logInForm.get('password');
  }

}
