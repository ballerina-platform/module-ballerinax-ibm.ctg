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
import io.ballerina.runtime.api.creators.ErrorCreator;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BError;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;

/**
 * {@code CommonUtils} contains the common utility functions for the Ballerina
 * IBM CTG connector.
 */
public class CommonUtils {

    private static final String IBM_CTG_ERROR = "Error";
    private static final BString PROGRAM_NAME = StringUtils.fromString("programName");
    private static final BString COMMAREA = StringUtils.fromString("commArea");
    private static final BString COMMAREA_LENGTH = StringUtils.fromString("commAreaSize");
    private static final BString TIMEOUT = StringUtils.fromString("timeout");
    private static final int MAX_COMMAREA_LENGTH = 32500;
    private static final int MIN_COMMAREA_LENGTH = 50;

    private CommonUtils() {
    }

    public static BError createError(String message) {
        return ErrorCreator.createError(
                ModuleUtils.getModule(), IBM_CTG_ERROR, StringUtils.fromString(message), null, null);
    }

    public static BError createError(String message, Throwable exception) {
        BError cause = ErrorCreator.createError(exception);
        return ErrorCreator.createError(
                ModuleUtils.getModule(), IBM_CTG_ERROR, StringUtils.fromString(message), cause, null);
    }

    public static ECIRequest getNativeRequest(String cicsServer, Auth authConfig, BMap<BString, Object> request) {
        int commAreaLength = MIN_COMMAREA_LENGTH;
        if (request.containsKey(COMMAREA_LENGTH)) {
            commAreaLength = request.getIntValue(COMMAREA_LENGTH).intValue();
            if (commAreaLength > MAX_COMMAREA_LENGTH) {
                commAreaLength = MAX_COMMAREA_LENGTH;
            }
        }

        byte[] commArea;
        if (!request.containsKey(COMMAREA)) {
            commArea = new byte[commAreaLength];
        } else {
            commArea = request.getArrayValue(COMMAREA).getByteArray();
        }

        ECIRequest nativeRequest = new ECIRequest(
            ECIRequest.ECI_SYNC,
            cicsServer, 
            authConfig.userId(),
            authConfig.password(),
            request.getStringValue(PROGRAM_NAME).getValue(),
            null,
            commArea,
            commAreaLength,
            ECIRequest.ECI_NO_EXTEND,
            ECIRequest.ECI_LUW_NEW
        );
        nativeRequest.setECITimeout(request.getIntValue(TIMEOUT).shortValue());
        return nativeRequest;
    }
}
