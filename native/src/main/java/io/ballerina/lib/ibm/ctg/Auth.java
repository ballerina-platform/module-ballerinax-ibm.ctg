/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com)
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ballerina.lib.ibm.ctg;

import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;

/**
 * {@code Auth} contains the java representation of the Ballerina IBM CTG client
 * authentication configurations.
 *
 * @param userId The CICS userId.
 * @param password The CICS password or password phrase.
 */
public record Auth(String userId, String password) {

    private static final BString USER_ID = StringUtils.fromString("userId");
    private static final BString PASSWORD = StringUtils.fromString("password");

    public Auth(BMap<BString, Object> authConfig) {
        this(
                authConfig.getStringValue(USER_ID).getValue(),
                authConfig.getStringValue(PASSWORD).getValue()
        );
    }
}
