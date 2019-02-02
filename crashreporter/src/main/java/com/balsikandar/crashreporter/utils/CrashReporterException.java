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
 * Represents an error condition specific to the Crash Reporter for Android.
 */
public class CrashReporterException extends RuntimeException {
    static final long serialVersionUID = 1;

    /**
     * Constructs a new CrashReporterException.
     */
    public CrashReporterException() {
        super();
    }

    /**
     * Constructs a new CrashReporterException.
     *
     * @param message the detail message of this exception
     */
    public CrashReporterException(String message) {
        super(message);
    }

    /**
     * Constructs a new CrashReporterException.
     *
     * @param format the format string (see {@link java.util.Formatter#format})
     * @param args   the list of arguments passed to the formatter.
     */
    public CrashReporterException(String format, Object... args) {
        this(String.format(format, args));
    }

    /**
     * Constructs a new CrashReporterException.
     *
     * @param message   the detail message of this exception
     * @param throwable the cause of this exception
     */
    public CrashReporterException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Constructs a new CrashReporterException.
     *
     * @param throwable the cause of this exception
     */
    public CrashReporterException(Throwable throwable) {
        super(throwable);
    }

    @Override
    public String toString() {
        // Throwable.toString() returns "CrashReporterException:{message}". Returning just "{message}"
        // should be fine here.
        return getMessage();
    }
}
