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

import com.ibm.ctg.client.ECIRequest;
import com.ibm.ctg.client.JavaGateway;
import io.ballerina.runtime.api.Environment;
import io.ballerina.runtime.api.Future;
import io.ballerina.runtime.api.creators.ValueCreator;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BError;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BObject;
import io.ballerina.runtime.api.values.BString;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Representation of {@link com.ibm.ctg.client.JavaGateway} with utility methods
 * to invoke as inter-op functions.
 */
public class NativeClientAdaptor {

    private static final String NATIVE_CLIENT = "nativeClient";
    private static final String AUTH_CONFIG = "auth";
    private static final String CICS_SERVER = "cicsServer";
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool(new IbmCtgThreadFactory());

    private NativeClientAdaptor() {
    }

    /**
     * Creates an IBM CTG native client with the provided configurations.
     *
     * @param bIbmCtgClient The Ballerina IBM CTG client object.
     * @param configurations IBM CTG client connection configurations.
     * @return A Ballerina `ctg:Error` if failed to initialize the native client with the provided configurations.
     */
    public static Object init(BObject bIbmCtgClient, BMap<BString, Object> configurations) {
        try {
            ConnectionConfig connectionConfig = new ConnectionConfig(configurations);
            JavaGateway nativeClient = initializeJavaGw(connectionConfig);
            if (!nativeClient.isOpen()) {
                nativeClient.open();
            }

            bIbmCtgClient.addNativeData(NATIVE_CLIENT, nativeClient);
            bIbmCtgClient.addNativeData(AUTH_CONFIG, connectionConfig.auth());
            bIbmCtgClient.addNativeData(CICS_SERVER, connectionConfig.cicsServer());
        } catch (Exception e) {
            String errorMsg = String.format("Error occurred while initializing the gateway connection: %s", 
                e.getMessage());
            return CommonUtils.createError(errorMsg, e);
        }
        return null;
    }

    private static JavaGateway initializeJavaGw(ConnectionConfig configurations) throws IOException {
        if (Objects.isNull(configurations.secureSocket())) {
            String gwUrl = String.format("tcp://%s:%d", configurations.host(), configurations.port());
            return new JavaGateway(gwUrl, configurations.port());
        }

        String gwUrl = String.format("ssl://%s:%d", configurations.host(), configurations.port());

        SecureSocket secureSocket = configurations.secureSocket();
        Properties sslProperties = new Properties();

        sslProperties.put(JavaGateway.SSL_PROP_KEYRING_CLASS, secureSocket.sslKeyring());
        if (Objects.nonNull(secureSocket.sslkeyringPassword())) {
            sslProperties.put(JavaGateway.SSL_PROP_KEYRING_PW, secureSocket.sslkeyringPassword());
        }
        if (Objects.nonNull(secureSocket.sslCipherSuites())) {
            sslProperties.put(JavaGateway.SSL_PROP_CIPHER_SUITES, secureSocket.sslCipherSuites());
        }

        return new JavaGateway(gwUrl, configurations.port(), sslProperties);
    }

    /**
     * Executes the specified CICS transaction gateway request and retrieves the results.
     *
     * @param env The Ballerina runtime environment.
     * @param bIbmCtgClient The Ballerina IBM CTG client object.
     * @param request The Ballerina IBC MTG `EciRequest`.
     * @return A Ballerina `ctg:Error` if there was an error while processing the request or else the response or null.
     */
    public static Object execute(Environment env, BObject bIbmCtgClient, BMap<BString, Object> request) {
        JavaGateway nativeClient = (JavaGateway) bIbmCtgClient.getNativeData(NATIVE_CLIENT);
        String cicsServer = (String) bIbmCtgClient.getNativeData(CICS_SERVER);
        Auth authConfig = (Auth) bIbmCtgClient.getNativeData(AUTH_CONFIG);
        ECIRequest eciRequest = CommonUtils.getNativeRequest(cicsServer, authConfig, request);
        Future future = env.markAsync();
        EXECUTOR_SERVICE.execute(() -> {
            try {
                int operationResponseCode = nativeClient.flow(eciRequest);
                if (operationResponseCode != 0) {
                    String errorMsg = String.format("Error occurred while executing the operation, respose code: %d", 
                        operationResponseCode);
                    BError operationExecError = CommonUtils.createError(errorMsg);
                    future.complete(operationExecError);
                }

                int cicsResponseCode = eciRequest.getCicsRc();
                if (ECIRequest.ECI_NO_ERROR != cicsResponseCode) {
                    String errorMsg = String.format("Received error response from CICS server, respose code: %d", 
                        cicsResponseCode);
                    BError cicsResponseError = CommonUtils.createError(errorMsg);
                    future.complete(cicsResponseError);
                }

                byte[] responseCommArea = eciRequest.Commarea;
                int commareaLength = eciRequest.Commarea_Length;
                if (commareaLength == 0) {
                    future.complete(null);
                }

                byte[] responsePayload = new byte[commareaLength];
                System.arraycopy(responseCommArea, 0, responsePayload, 0, commareaLength);
                BArray clientResponse = ValueCreator.createArrayValue(responsePayload);
                future.complete(clientResponse);
            } catch (Exception e) {
                String errorMsg = String.format("Error occurred while executing the operation: %s", 
                        e.getMessage());
                BError bError = CommonUtils.createError(errorMsg, e);
                future.complete(bError);
            }
        });
        return null;
    }

    /**
     * Closes the IBM CTG client native resources.
     *
     * @param bIbmCtgClient The Ballerina IBM CTG client object.
     * @return A Ballerina `ctg:Error` if failed to close the underlying resources.
     */
    public static Object close(BObject bIbmCtgClient) {
        JavaGateway nativeClient = (JavaGateway) bIbmCtgClient.getNativeData(NATIVE_CLIENT);
        try {
            if (nativeClient.isOpen()) {
                nativeClient.close();
            }
        } catch (Exception e) {
            String errorMsg = String.format("Error occurred while closing the gateway connection: %s",
                    e.getMessage());
            return CommonUtils.createError(errorMsg, e);
        }
        return null;
    }
}
