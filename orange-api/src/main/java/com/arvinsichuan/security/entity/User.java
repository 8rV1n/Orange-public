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

package com.arvinsichuan.security.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Project theWhiteSail
 * <p>
 * Author: arvinsc@foxmail.com
 * <p>
 * Date: 2017/10/1
 * <p>
 * Package: com.arvinsichuan.users.entity
 *
 * @author ArvinSiChuan
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode
public class User implements Serializable {

    private static final long serialVersionUID = 4683637038104129460L;

    @Id
    @Column(name = "username", length = 16, nullable = false)
    @Length(min = 1, max = 16, message = "Username length should be more than 0 and less than 16")
    private String username;

    @Column(name = "password", length = 128, nullable = false)
    @Length(min = 8, message = "Password length should be equal or more than 8.")
    private String password;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "userByUsername", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Authority> authorities;


    public User() {
    }

    public User(String username, String password, Boolean enabled, List<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }
}
