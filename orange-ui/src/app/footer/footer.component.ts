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

import { Component, OnInit } from '@angular/core';
import {BasicInfo} from './beans/BasicInfo';
import {BasicInfoService} from './services/basic-info.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {

  info: BasicInfo = new BasicInfo('owner', '/', '', 1900);

  yearTo: number = new Date().getFullYear();



  constructor(private basicInfoService: BasicInfoService) {
  }

  ngOnInit() {
    this.updateInfo();
  }

  private updateInfo(): void {
    this.basicInfoService.getBasicInfo().subscribe(theInfo => this.info = theInfo);
  }
}
