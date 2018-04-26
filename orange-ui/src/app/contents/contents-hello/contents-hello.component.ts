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

@Component({
  selector: 'app-contents-hello',
  templateUrl: './contents-hello.component.html',
  styleUrls: ['./contents-hello.component.css']
})
export class ContentsHelloComponent implements OnInit {
  private _helloMsgBrief = 'Hello';

  private _helloMsg = 'Be an user in the system and continue.';

  validateForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this._createFrom();
  }

  ngOnInit() {

  }

  private _createFrom() {
    this.validateForm = this.fb.group({
      userName: [null, [Validators.required]],
      password: [null, [Validators.required]],
      remember: [true],
    });
  }

  _submitForm() {
    if (this.validateForm.controls) {
      for (const i of Object.keys(this.validateForm.controls)) {
        this.validateForm.controls[i].markAsDirty();
      }
    }
  }

  get helloMsgBrief(): string {
    return this._helloMsgBrief;
  }

  get helloMsg(): string {
    return this._helloMsg;
  }


  get userName() {
    return this.validateForm.get('userName');
  }

  get password() {
    return this.validateForm.get('password');
  }

}
