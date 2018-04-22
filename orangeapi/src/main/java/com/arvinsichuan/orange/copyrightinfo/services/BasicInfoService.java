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

package com.arvinsichuan.orange.copyrightinfo.services;

import com.arvinsichuan.orange.copyrightinfo.entities.CopyrightInfo;
import com.arvinsichuan.orange.copyrightinfo.repositories.CopyrightInfoRepo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Project Just4Fun.NoMoreWait
 * <p>
 * Author: arvinsc@foxmail.com
 * <p>
 * Date: 18-Mar-18
 * <p>
 * Package: com.arvinischuan.just4fun.nomorewait.basicinfo.services
 *
 * @author Arvin
 */
@Service(value = "copyright-info-service")
public class BasicInfoService {

    @Resource(name = "copyright-info-repo", type = CopyrightInfoRepo.class)
    private CopyrightInfoRepo copyrightInfoRepo;

    public CopyrightInfo getBasicInfo() {
        CopyrightInfo info = null;
        if (copyrightInfoRepo.findAll().iterator().hasNext()) {
            info = copyrightInfoRepo.findAll().iterator().next();
        }
        return info;
    }
}
