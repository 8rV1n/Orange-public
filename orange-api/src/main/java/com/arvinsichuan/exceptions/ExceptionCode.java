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

/**
 * Project Orange-public
 *
 * @author: arvinsichuan
 * <p>
 * Date: 24-Apr-18
 * <p>
 * Package: com.arvinsichuan.exceptions
 */
public enum ExceptionCode {

    /**
     * Empty Data Exception
     */
    EMPTY_DATA(0, EmptyDataException.class),
    DUPLICATED_DATA(1, DuplicatedDataException.class);

    private int code;
    private Class<? extends AbstractCustomizedException> exception;

    @SuppressWarnings("unused")
    ExceptionCode(int code, Class<? extends AbstractCustomizedException> exception) {
        this.code = code;
        this.exception = exception;
    }

    public int getCode() {
        return this.code;
    }

    public Class<? extends AbstractCustomizedException> getExceptionClass() {
        return this.exception;
    }
}
