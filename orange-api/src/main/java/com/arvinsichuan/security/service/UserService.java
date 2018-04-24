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

package com.arvinsichuan.security.service;


import com.arvinsichuan.exceptions.DuplicatedDataException;
import com.arvinsichuan.generalapi.WebInfoEntity;
import com.arvinsichuan.security.entity.AuthoritiesEnum;
import com.arvinsichuan.security.entity.Authority;
import com.arvinsichuan.security.entity.User;
import com.arvinsichuan.security.repository.UserRepository;
import com.arvinsichuan.generalapi.defaultimpl.WebInfoEntityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Project theWhiteSail
 * <p>
 * Author: arvinsc@foxmail.com
 * <p>
 * Date: 2017/10/2
 * <p>
 * Package: com.arvinsichuan.thewhitesail.security.service
 *
 * @author Arvin
 */
@Service("userService")
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource(name = "userRepository")
    private UserRepository userRepository;

    @Resource(name = "passEncoder")
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = DuplicatedDataException.class)
    public WebInfoEntity<User> userSignUp(String username, String password) throws DuplicatedDataException {
        WebInfoEntity<User> webInfo = new WebInfoEntityImpl<>();
        Optional<User> userOptional = userRepository.findById(username);

        if (userOptional.isPresent()) {
            throw new DuplicatedDataException("User.username value: " + username);
        } else {
            User user = getANewUser(username, password);
            try {
                userRepository.save(user);
                user.setPassword("[PROTECTED]");
                webInfo.isOK();
                webInfo.addData(user);
            } catch (Exception e) {
                logger.error("Exception in saving", e);
                webInfo.haveException(e, "Exception in saving");
            }
        }
        return webInfo;
    }


    private User getANewUser(String username, String password) {
//        Process username to lowercase
        username = username.toLowerCase();

//          Encode passwords
        password = passwordEncoder.encode(password);

//          Build an user instance
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(true);

//          Assign Authorities
        Authority authority = new Authority();
        authority.setUserByUsername(user);
        authority.setOwnedAuthority(AuthoritiesEnum.ROLE_USER);
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authority);
        user.setAuthorities(authorities);

        return user;
    }
}
