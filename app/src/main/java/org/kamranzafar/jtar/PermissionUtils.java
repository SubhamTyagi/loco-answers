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

package org.kamranzafar.jtar;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Helps dealing with file permissions.
 */
public class PermissionUtils {

    private static final boolean isPosix = FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
    private static Map<PosixFilePermission, Integer> posixPermissionToInteger = new HashMap<>();

    static {
        posixPermissionToInteger.put(PosixFilePermission.OWNER_EXECUTE, 0100);
        posixPermissionToInteger.put(PosixFilePermission.OWNER_WRITE, 0200);
        posixPermissionToInteger.put(PosixFilePermission.OWNER_READ, 0400);

        posixPermissionToInteger.put(PosixFilePermission.GROUP_EXECUTE, 0010);
        posixPermissionToInteger.put(PosixFilePermission.GROUP_WRITE, 0020);
        posixPermissionToInteger.put(PosixFilePermission.GROUP_READ, 0040);

        posixPermissionToInteger.put(PosixFilePermission.OTHERS_EXECUTE, 0001);
        posixPermissionToInteger.put(PosixFilePermission.OTHERS_WRITE, 0002);
        posixPermissionToInteger.put(PosixFilePermission.OTHERS_READ, 0004);
    }

    /**
     * Get file permissions in octal mode, e.g. 0755.
     * <p>
     * Note: it uses `java.nio.file.attribute.PosixFilePermission` if OS supports this, otherwise reverts to
     * using standard Java file operations, e.g. `java.io.File#canExecute()`. In the first case permissions will
     * be precisely as reported by the OS, in the second case 'owner' and 'group' will have equal permissions and
     * 'others' will have no permissions, e.g. if file on Windows OS is `read-only` permissions will be `0550`.
     *
     * @throws NullPointerException     if file is null.
     * @throws IllegalArgumentException if file does not exist.
     */
    public static int permissions(File f) {
        if (f == null) {
            throw new NullPointerException("File is null.");
        }
        if (!f.exists()) {
            throw new IllegalArgumentException("File " + f + " does not exist.");
        }

        return isPosix ? posixPermissions(f) : standardPermissions(f);
    }

    private static int posixPermissions(File f) {
        int number = 0;
        try {
            Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(f.toPath());
            for (Map.Entry<PosixFilePermission, Integer> entry : posixPermissionToInteger.entrySet()) {
                if (permissions.contains(entry.getKey())) {
                    number += entry.getValue();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return number;
    }

    private static Set<StandardFilePermission> readStandardPermissions(File f) {
        Set<StandardFilePermission> permissions = new HashSet<>();
        if (f.canExecute()) {
            permissions.add(StandardFilePermission.EXECUTE);
        }
        if (f.canWrite()) {
            permissions.add(StandardFilePermission.WRITE);
        }
        if (f.canRead()) {
            permissions.add(StandardFilePermission.READ);
        }
        return permissions;
    }

    private static Integer standardPermissions(File f) {
        int number = 0;
        Set<StandardFilePermission> permissions = readStandardPermissions(f);
        for (StandardFilePermission permission : permissions) {
            number += permission.mode;
        }
        return number;
    }

    /**
     * XXX: When using standard Java permissions, we treat 'owner' and 'group' equally and give no
     * permissions for 'others'.
     */
    private static enum StandardFilePermission {
        EXECUTE(0110), WRITE(0220), READ(0440);

        private int mode;

        private StandardFilePermission(int mode) {
            this.mode = mode;
        }
    }
}
