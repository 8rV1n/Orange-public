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

package com.arvinsichuan.security.utils;


import com.arvinsichuan.security.entity.AuthoritiesEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project theWhiteSail
 * <p>
 * Author: arvinsc@foxmail.com
 * <p>
 * Date: 2017/10/1
 * <p>
 * Package: com.arvinsichuan.security.utils
 *
 * @author ArvinSiChuan
 */
@Slf4j
public class SecurityInfo {

    private SecurityInfo() {
    }

    private static Object retrievePrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }

    public static Principal getPrincipal() {
        Object principal = retrievePrincipal();
        if (principal instanceof Principal) {
            return ((Principal) principal);
        }
        return null;
    }

    public static UserDetails getUserDetails() {
        Object principal = retrievePrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal);
        }
        return null;
    }

    /**
     * Retrieve Username.
     *
     * @return username
     */
    public static String getUsername() {
        Principal principal = getPrincipal();
        UserDetails userDetails = getUserDetails();
        if (userDetails != null) {
            return userDetails.getUsername();
        } else if (principal != null) {
            return principal.getName();
        } else {
            return AuthoritiesEnum.ROLE_ANONYMOUS.name();
        }
    }

    /**
     * Retrieve the top authentication
     *
     * @return top authority
     */
    public static AuthoritiesEnum getTopAuth() {
        Iterator iterator = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator();
        List<AuthoritiesEnum> authorities = new ArrayList<>();
        while (iterator.hasNext()) {
            authorities.add(AuthoritiesEnum.valueOf(iterator.next().toString()));
        }
        authorities = authorities.stream().sorted().collect(Collectors.toList());
        return authorities.get(0);
    }
}