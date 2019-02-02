/*
 *   Copyright (C) 2018 SHUBHAM TYAGI
 *
 *    This file is part of Trivia Hack.
 *     Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not
 *     use this file except in compliance with the License. You may obtain a copy of
 *     the License at
 *
 *     https://www.gnu.org/licenses/gpl-3.0
 *
 *    Trivia Hack is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Trivia Hack.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *
 */

package com.balsikandar.crashreporter.utils;

/**
 * Created by bali on 02/08/17.
 */

/**
 * An Exception indicating that the Crash Reporter has not been correctly initialized.
 */
public class CrashReporterNotInitializedException extends CrashReporterException {
    static final long serialVersionUID = 1;

    /**
     * Constructs a CrashReporterNotInitializedException with no additional information.
     */
    public CrashReporterNotInitializedException() {
        super();
    }

    /**
     * Constructs a CrashReporterNotInitializedException with a message.
     *
     * @param message A String to be returned from getMessage.
     */
    public CrashReporterNotInitializedException(String message) {
        super(message);
    }

    /**
     * Constructs a CrashReporterNotInitializedException with a message and inner error.
     *
     * @param message   A String to be returned from getMessage.
     * @param throwable A Throwable to be returned from getCause.
     */
    public CrashReporterNotInitializedException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Constructs a CrashReporterNotInitializedException with an inner error.
     *
     * @param throwable A Throwable to be returned from getCause.
     */
    public CrashReporterNotInitializedException(Throwable throwable) {
        super(throwable);
    }
}