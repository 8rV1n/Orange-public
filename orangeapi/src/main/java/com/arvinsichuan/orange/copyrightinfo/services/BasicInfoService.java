/*
 * MIT License
 *
 * Copyright (c) 2018
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
