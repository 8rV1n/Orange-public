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

package com.arvinsichuan.generalapi;

import com.arvinsichuan.exceptions.AbstractCustomizedException;
import com.arvinsichuan.exceptions.DuplicatedDataException;
import com.arvinsichuan.exceptions.EmptyDataException;

import java.io.Serializable;
import java.util.Map;

/**
 * Project Orange-public
 *
 * @author: arvinsichuan
 * <p>
 * Date: 24-Apr-18
 * <p>
 * Package: com.arvinsichuan.security.utils
 */
public interface WebInfoEntity<T> extends Map<String, Object>, Serializable {


    public enum Keys {
        /**
         * Status key in the api
         */
        STATUS_KEY("status"),
        /**
         * Code key in the api
         */
        CODE_KEY("code"),
        /**
         * Message key in the api
         */
        MSG_KEY("message"),
        DATA_KEY("data"),
        INFO_KEY("info");

        private String keyName;

        Keys(String keyName) {
            this.keyName = keyName;
        }

        public String getKeyName() {
            return this.keyName;
        }

    }

    public enum Status {
        /**
         * OK - Status OK
         * Status code starts with 0 (0[0-9]*)
         */
        OK,
        /*
         *  EXCEPTION - Has Exception or have Exceptions
         *  Error code starts with prefix 1 (1[0-9]*)
         */
        EXCEPTION,
        /**
         * non of data found Error code 2
         */
        EMPTY_DATA,
        /*
         *   Duplicated data found which can not be.
         *   Error code 3
         */
        DUPLICATE_DATA
    }



    /**
     * No exception status in the switch api
     *
     * @return WebInfoEntity<T>
     */
    public default WebInfoEntity<T> isOK() {
        put(Keys.STATUS_KEY.keyName, Status.OK);
        put(Keys.CODE_KEY.keyName, Status.OK.ordinal());
        return this;
    }


    /**
     * Mark an exception in the switch api
     *
     * @param e The Exception that need to be mark
     * @return WebInfoEntity<T>
     */
    public default WebInfoEntity<T> haveException(Exception e) {
        put(Keys.STATUS_KEY.keyName, Status.EXCEPTION);
        int code;
        if (e instanceof AbstractCustomizedException) {
            code = ((AbstractCustomizedException) e).getCode();
        } else {
            code = Status.EXCEPTION.ordinal();
        }
        put(Keys.CODE_KEY.keyName, code);
        put(Keys.MSG_KEY.keyName, e.getMessage());
        return this;
    }

    /**
     * Mark an exception in the switch api with extra given message
     *
     * @param e       The Exception that need to be mark
     * @param message The given extra message in the returned api
     * @return WebInfoEntity<T>
     */
    public default WebInfoEntity<T> haveException(Exception e, String message) {
        haveException(e);
        put(Keys.MSG_KEY.keyName, e.getMessage() + "; Additional Message:" + message);
        return this;
    }


    /**
     * Add Data to the switch api
     *
     * @param t   Data to add
     * @return WebInfoEntity<T>
     */
    public default WebInfoEntity<T> addData(T t) {
        put(Keys.DATA_KEY.keyName, t);
        return this;
    }

    /**
     * Get the added data
     * @return T
     */
    @SuppressWarnings("unchecked cast")
    public default T getData(){
        return (T)get(Keys.DATA_KEY.keyName);
    }


    /**
     * Add information to the switch api
     *
     * @param info Info to add
     * @return WebInfoEntity<T>
     */
    public default WebInfoEntity<T> addInfo(String info) {
        put(Keys.INFO_KEY.keyName, info);
        return this;
    }


    /**
     * Mark an empty data status to the switch api
     * @return WebInfoEntity<T>
     */
    public default WebInfoEntity<T> emptyData() {
        return haveException(new EmptyDataException(Status.EMPTY_DATA.toString()));
    }

    /**
     * Mark an empty data status to the switch api with extra message
     * @param message The extra message
     * @return WebInfoEntity<T>
     */
    public default WebInfoEntity<T> emptyData(String message){
        return haveException(new EmptyDataException(Status.EMPTY_DATA.toString()),message);
    }

    /**
     * Mark a duplicated exception status to the switch api.
     * @return WebInfoEntity<T>
     */
    public default WebInfoEntity<T> duplicatedData(){
        return haveException(new DuplicatedDataException(Status.DUPLICATE_DATA.toString()));
    }
    /**
     * Mark a duplicated exception status to the switch api with extra message.
     *
     * @param message The extra message
     * @return WebInfoEntity<T>
     */
    public default WebInfoEntity<T> duplicatedData(String message){
        return haveException(new DuplicatedDataException(Status.DUPLICATE_DATA.toString()),message);
    }


}
