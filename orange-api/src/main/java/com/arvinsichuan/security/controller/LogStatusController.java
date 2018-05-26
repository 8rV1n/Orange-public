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

package com.arvinsichuan.security.controller;


import com.arvinsichuan.generalapi.WebInfoEntity;
import com.arvinsichuan.generalapi.defaultimpl.WebInfoEntityImpl;
import com.arvinsichuan.security.utils.SecurityInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.arvinsichuan.generalapi.WebInfoEntity.*;

/**
 * Project theWhiteSail
 * <p>
 * Author: arvinsc@foxmail.com
 * <p>
 * Date: 2017/10/1
 * <p>
 * Package: com.arvinsichuan.auth
 *
 * @author ArvinSiChuan
 */
@RestController
@RequestMapping(path = "/auth")
@Slf4j
public class LogStatusController {

    @RequestMapping(path = "/status", method = {RequestMethod.POST, RequestMethod.GET})
    public WebInfoEntity<Map<String, String>> getLoginStatus(HttpServletRequest request, HttpServletResponse response) {
        WebInfoEntity<Map<String, String>> info = assembleAuthInfo();
        if ((Integer) info.getOrDefault(Keys.CODE_KEY.getKeyName(), -1) != 0) {
            Arrays.stream(request.getCookies()).forEach(cookie -> {
                cookie.setValue(null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            });
        }
        return info;
    }

    @GetMapping(path = "/login")
    public WebInfoEntity<Map<String, String>> loginPageInfo() {
        WebInfoEntity<Map<String, String>> info = assembleAuthInfo();
        Map<String, String> infoMsg = info.getData();
        infoMsg.put("loginEndpoint", "/auth/login");
        return info;
    }


    private WebInfoEntity<Map<String, String>> assembleAuthInfo() {
        WebInfoEntity<Map<String, String>> info = new WebInfoEntityImpl<>();
        info.isOK();
        Map<String, String> infoMsg = new HashMap<>(2);
        infoMsg.put(SecurityInfo.getTopAuth().name(), String.valueOf(SecurityInfo.getTopAuth().ordinal()));
        info.addData(infoMsg);
        return info;
    }
}
