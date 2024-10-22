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
 * {@code ConnectionConfig} contains the java representation of the Ballerina
 * IBM CTG client configurations.
 *
 * @param host The hostname of the IBM CICS transaction gateway.
 * @param port The port in which the IBM CICS transaction gateway is running.
 * @param cicsServer The name of the CICS server.
 * @param socketConnectTimeout The timeout value (in seconds) to allow a socket to connect to a remote Gateway daemon.
 * @param auth The authentication credentials for CICS.
 * @param secureSocket The SSL configurations.
 */
public record ConnectionConfig(String host, int port, String cicsServer, int socketConnectTimeout, Auth auth, 
                               SecureSocket secureSocket) {

    private static final BString HOST = StringUtils.fromString("host");
    private static final BString PORT = StringUtils.fromString("port");
    private static final BString CICS_SERVER = StringUtils.fromString("cicsServer");
    private static final BString SOCKET_CONNECT_TIMEOUT = StringUtils.fromString("socketConnectTimeout");
    private static final BString AUTH = StringUtils.fromString("auth");
    private static final BString SECURE_SOCKET = StringUtils.fromString("secureSocket");

    public ConnectionConfig(BMap<BString, Object> configurtions) {
        this(
                configurtions.getStringValue(HOST).getValue(),
                configurtions.getIntValue(PORT).intValue(),
                configurtions.getStringValue(CICS_SERVER).getValue(),
                configurtions.getIntValue(SOCKET_CONNECT_TIMEOUT).intValue(),
                new Auth((BMap<BString, Object>) configurtions.getMapValue(AUTH)),
                configurtions.containsKey(SECURE_SOCKET)
                ? new SecureSocket((BMap<BString, Object>) configurtions.getMapValue(SECURE_SOCKET)) : null
        );
    }
}
