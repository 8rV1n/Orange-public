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

package com.arvinsichuan.orange.copyrightinfo.entities;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * Project Just4Fun.NoMoreWait
 * <p>
 * Author: arvinsc@foxmail.com
 * <p>
 * Date: 18-Mar-18
 * <p>
 * Package: com.arvinischuan.just4fun.nomorewait.basicinfo.entities
 *
 * @author Arvin
 */
@Entity
@Table(name = "basic_info")
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
public class CopyrightInfo {
    @Id
    @Getter
    @Setter
    @Column(name = "copyright_owner", length = 64, nullable = false)
    @NotBlank(message = "copyright owner shouldn't be empty.")
    private String copyrightOwner;

    @Getter
    @Setter
    @Column(name = "copyright_owner_link", length = 255)
    private String copyrightOwnerLink;

    @Getter
    @Setter
    @Column(name = "info", length = 255)
    @Length(max = 255, message = "Info too long.")
    private String info;

    @Getter
    @Setter
    @Min(value = 1000, message = "year should be more than 1000.")
    @Column(name = "year_from", length = 4, nullable = false)
    private int yearFrom = 1000;

}
