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
