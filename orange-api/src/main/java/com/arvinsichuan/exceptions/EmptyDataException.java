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

package com.arvinsichuan.exceptions;

import java.io.Serializable;

/**
 * Project PancakeofMountHuang
 * <p>
 * Author: arvinsc@foxmail.com
 * <p>
 * Date: 30-Oct-17
 * <p>
 * Package: com.arvinsichuan.general.exceptions
 * @author ArvinSiChuan
 */
public class EmptyDataException extends AbstractCustomizedException implements Serializable {
    private static final long serialVersionUID = -8473288004413293738L;

    private static final int CODE=ExceptionCode.EMPTY_DATA.getCode();

    public EmptyDataException(String message) {
        super(message);
    }

    @Override
    public int getCode() {
        return CODE;
    }
}
