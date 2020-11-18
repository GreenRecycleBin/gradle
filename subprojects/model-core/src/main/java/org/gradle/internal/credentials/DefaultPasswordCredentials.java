/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.internal.credentials;

import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.api.tasks.Internal;

public class DefaultPasswordCredentials implements PasswordCredentials {
    private String username;
    private String password;

    public DefaultPasswordCredentials() {
    }

    public DefaultPasswordCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Internal
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Internal
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("Credentials [username: %s]", username);
    }
}
