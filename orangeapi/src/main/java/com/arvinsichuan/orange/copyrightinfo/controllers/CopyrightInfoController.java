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

package com.arvinsichuan.orange.copyrightinfo.controllers;

import com.arvinsichuan.orange.copyrightinfo.entities.CopyrightInfo;
import com.arvinsichuan.orange.copyrightinfo.services.BasicInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Project Just4Fun.NoMoreWait
 * <p>
 * Author: arvinsc@foxmail.com
 * <p>
 * Date: 18-Mar-18
 * <p>
 * Package: com.arvinischuan.just4fun.nomorewait.basicinfo.controllers
 *
 * @author 75744
 */
@RestController
@RequestMapping(value = "/copyright")
public class CopyrightInfoController {

    @Resource(name = "copyright-info-service", type = BasicInfoService.class)
    private BasicInfoService basicInfoService;

    @GetMapping("/")
    public CopyrightInfo getBasicInfo() {
        return basicInfoService.getBasicInfo();
    }

    @GetMapping("/owner")
    public String getBasicInfoOwner() {
        return basicInfoService.getBasicInfo().getCopyrightOwner();
    }
}
