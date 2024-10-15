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
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;

/**
 * {@code Auth} contains the java representation of the Ballerina IBM CTG client
 * SSL configurations.
 *
 * @param sslKeyring The SSL keyring.
 * @param sslkeyringPassword The password for the keyring.
 * @param sslCipherSuites The cipherSuites parameter can be used when
 * establishing an SSL connection.
 */
public record SecureSocket(String sslKeyring, String sslkeyringPassword, String sslCipherSuites) {

    private static final BString SSL_KEYRING = StringUtils.fromString("sslKeyring");
    private static final BString SSL_KEYRING_PASSWORD = StringUtils.fromString("sslkeyringPassword");
    private static final BString SSL_CIPHER_SUITS = StringUtils.fromString("sslCipherSuites");

    public SecureSocket(BMap<BString, Object> secureSocketConfig) {
        this(
                secureSocketConfig.getStringValue(SSL_KEYRING).getValue(),
                secureSocketConfig.containsKey(SSL_KEYRING_PASSWORD)
                ? secureSocketConfig.getStringValue(SSL_KEYRING_PASSWORD).getValue() : null,
                getCipherSuites(secureSocketConfig)
        );
    }

    private static String getCipherSuites(BMap<BString, Object> secureSocketConfig) {
        if (!secureSocketConfig.containsKey(SSL_CIPHER_SUITS)) {
            return null;
        }
        
        BArray bCicpherSuites = secureSocketConfig.getArrayValue(SSL_CIPHER_SUITS);
        if (bCicpherSuites.isEmpty()) {
            return null;
        }

        String[] cipherSuites = bCicpherSuites.getStringArray();
        return String.join(",", cipherSuites);
    } 
}
