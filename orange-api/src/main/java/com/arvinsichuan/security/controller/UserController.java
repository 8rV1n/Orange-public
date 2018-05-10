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


import com.arvinsichuan.exceptions.DuplicatedDataException;
import com.arvinsichuan.generalapi.WebInfoEntity;
import com.arvinsichuan.generalapi.defaultimpl.WebInfoEntityImpl;
import com.arvinsichuan.security.entity.User;
import com.arvinsichuan.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

/**
 * Project theWhiteSail
 * <p>
 * Author: arvinsc@foxmail.com
 * <p>
 * Date: 2017/9/28
 * <p>
 * Package: com.arvinsichuan.users.controller
 *
 * @author Arvin
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    @PostMapping(path = "/signUp")
    public WebInfoEntity signUp(@RequestBody User user) {
        WebInfoEntity webInfoEntity = new WebInfoEntityImpl<>();
        try {
            webInfoEntity = userService.userSignUp(user.getUsername(), user.getPassword());
        } catch (DuplicatedDataException | ConstraintViolationException e) {
            webInfoEntity.haveException(e);
        }
        return webInfoEntity;
    }

}
