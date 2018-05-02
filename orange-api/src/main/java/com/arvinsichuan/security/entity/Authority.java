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

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Project theWhiteSail
 * <p>
 * Author: arvinsc@foxmail.com
 * <p>
 * Date: 2017/10/1
 * <p>
 * Package: com.arvinsichuan.users.entity
 * @author Arvin
 */
@Entity
@Table(name = "authorities")
@IdClass(AuthorityKey.class)
public class Authority implements Serializable {
    private static final long serialVersionUID = -8051333978047192441L;

    @Id
    @Column(name = "authority", length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthoritiesEnum ownedAuthority;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username",
            insertable = false, nullable = false, foreignKey = @ForeignKey(name = "fk_authorities_users"))
    @JsonBackReference
    private User userByUsername;

    public Authority() {
    }

    public Authority(AuthoritiesEnum ownedAuthority, User userByUsername) {
        this.ownedAuthority = ownedAuthority;
        this.userByUsername = userByUsername;
    }



    public AuthoritiesEnum getOwnedAuthority() {
        return ownedAuthority;
    }

    public void setOwnedAuthority(AuthoritiesEnum ownedAuthority) {
        this.ownedAuthority = ownedAuthority;
    }

    public User getUserByUsername() {
        return userByUsername;
    }

    public void setUserByUsername(User userByUsername) {
        this.userByUsername = userByUsername;
    }


}
