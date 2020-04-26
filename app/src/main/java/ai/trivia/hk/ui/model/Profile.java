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

public class Profile {

    private String name;
    private float x1, x2, y1, y2;

    public Profile() {
    }

    public Profile(String name, float x1, float y1, float x2, float y2) {
        this.name = name;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public String getName() {

        return name;
    }

    public Profile setName(String name) {
        this.name = name;
        return this;
    }

    public float getX1() {
        return x1;
    }

    public Profile setX1(float x1) {
        this.x1 = x1;
        return this;
    }

    public float getX2() {
        return x2;
    }

    public Profile setX2(float x2) {
        this.x2 = x2;
        return this;
    }

    public float getY1() {
        return y1;
    }

    public Profile setY1(float y1) {
        this.y1 = y1;
        return this;
    }

    public float getY2() {
        return y2;
    }

    public Profile setY2(float y2) {
        this.y2 = y2;
        return this;
    }
}
