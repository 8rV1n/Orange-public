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
import {NzMessageService, UploadFile} from 'ng-zorro-antd';
import {LogLevel, LogService} from '../../base-services/log.service';
import {Router} from "@angular/router";
import {AuthenticationService} from "../../authentication/authentication.service";

@Component({
  selector: 'app-detect-plate',
  templateUrl: './contents-detection.component.html',
  styleUrls: ['./contents-detection.component.css']
})
export class ContentsDetectionComponent implements OnInit {
  plateResult: UploadFile[] = [];
  previewImage: UploadFile;
  previewImageUrl = '';
  previewVisible = false;
  private msgId = null;
  concurrentLimit = 0;


  constructor(private msgService: NzMessageService,
              private authService: AuthenticationService,
              private router: Router) {
  }

  ngOnInit() {
    this.checkAuth();
  }

  private checkAuth() {
    const authenticated = AuthenticationService.checkAuth();
    if (!authenticated) {
      this.router.navigate(['']);
    }
  }


  handleChange({file, fileList}) {
    const status = file.status;
    if (status === 'uploading') {
      if (this.msgId === null) {
        this.msgId = this.msgService.loading(`${file.name} uploading, please wait a moment.`, {nzDuration: 0}).messageId;
      }
    }
    if (status === 'done') {
      this.msgId = this.removeMsg(this.msgId);
      this.msgService.success(`${file.name} upload successfully.`);
      const result = file.response;
      LogService.logDev(LogLevel.INFO, result);
      this.plateResult.push(file);
    } else if (status === 'error') {
      this.msgId = this.removeMsg(this.msgId);
      this.msgService.error(`${file.name} upload failed.`);
    }
  }

  handlePreview = (file: UploadFile) => {
    for (const img of this.plateResult) {
      if (img.uid === file.uid) {
        LogService.logDev(LogLevel.INFO, img);
        this.previewImage = img;
        this.previewImageUrl = img.url || img.thumbUrl;
        this.previewVisible = true;
      }
    }
  }


  private removeMsg(id) {
    if (id !== null) {
      this.msgService.remove(id);
      return null;
    }
    return id;
  }


}
