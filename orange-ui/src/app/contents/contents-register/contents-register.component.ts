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
import {User} from '../../authentication/user';
import {AuthenticationService} from '../../authentication/authentication.service';
import {NzMessageService} from 'ng-zorro-antd';
import {Router} from '@angular/router';

@Component({
  selector: 'app-contents-register',
  templateUrl: './contents-register.component.html',
  styleUrls: ['./contents-register.component.css']
})
export class ContentsRegisterComponent implements OnInit {
  get registerData() {
    return this._registerData;
  }

  set registerData(value) {
    this._registerData = value;
  }

  private _helloMsgBrief = 'Register';

  private _helloMsg = 'Complete the form and continue.';

  private _registerData;

  registerForm: FormGroup;

  constructor(private fb: FormBuilder,
              private authService: AuthenticationService,
              private msgService: NzMessageService,
              private router: Router) {
    this._createFrom();
  }

  ngOnInit() {

  }

  private _createFrom() {
    this.registerForm = this.fb.group({
      username: [null, [Validators.required, Validators.maxLength(16)]],
      password: [null, [Validators.required, Validators.minLength(8)]],
      passwordAgain: [null, [Validators.required]],
    });
  }

  _submitForm() {
    if (this.registerForm.controls) {
      for (const i of Object.keys(this.registerForm.controls)) {
        this.registerForm.controls[i].markAsDirty();
      }
      const msgId = this.msgService.loading('Submitting', {nzDuration: 0}).messageId;
      this.authService.register(this._prepareUser()).subscribe(switchApi => {
        this._registerData = switchApi;
        this.msgService.remove(msgId);
        if (this.registerData) {
          if (this.registerData.code === 1) {
            this.msgService.error('Username has been used, please choose another one or just go login', {nzDuration: 8000});
          } else if (this.registerData.code === 0) {
            this.msgService.success('Registering Success, please login.', {nzDuration: 3000});
            this.router.navigate(['']);
          }
        }
      });
    }
  }


  private _prepareUser(): User {
    const formModel = this.registerForm.value;
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
    return this.registerForm.get('username');
  }

  get password() {
    return this.registerForm.get('password');
  }

  get passwordAgain() {
    return this.registerForm.get('passwordAgain');
  }


}
