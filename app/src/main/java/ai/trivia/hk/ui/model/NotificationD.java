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

package ai.trivia.hk.ui.model;

public class NotificationD {
    private String title;
    private String message;
    private String iconUrl;
    private String action;
    private String actionDestination;

    public String getTitle() {
        return title;
    }

    public NotificationD setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public NotificationD setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public NotificationD setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        return this;
    }

    public String getAction() {
        return action;
    }

    public NotificationD setAction(String action) {
        this.action = action;
        return this;
    }

    public String getActionDestination() {
        return actionDestination;
    }

    public NotificationD setActionDestination(String actionDestination) {
        this.actionDestination = actionDestination;
        return this;
    }
}
