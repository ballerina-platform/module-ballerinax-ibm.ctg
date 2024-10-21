// Copyright (c) 2024 WSO2 LLC. (http://www.wso2.com).
//
// WSO2 LLC. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/jballerina.java;

# IBM CTG client.
public isolated client class Client {

    # Initialize the Ballerina IBM CTG client.
    # ```ballerina
    # ctg:ConnectionConfig congig = ...;
    # ctg:Client ctg = check new(config);
    # ```
    #
    # + configs - The IBM CTG client configurations
    # + return - The `ctg:Client` or an `ctg:Error` if the initialization failed
    public isolated function init(*ConnectionConfig configs) returns Error? {
        return self.externInit(configs);
    }

    isolated function externInit(ConnectionConfig configs) returns Error? =
    @java:Method {
        name: "init",
        'class: "io.ballerina.lib.ibm.ctg.NativeClientAdaptor"
    } external;

    # Executes the specified CICS transaction gateway request and retrieves the results.
    # ```ballerina
    # EciRequest request = ...;
    # byte[]? response = check ctg->execute(request);
    # ```
    # 
    # + request - The `ctg:EciRequest` request with relevant details
    # + return - A `byte[]`, nil, or else a `ctg:Error` if the operation failed.
    isolated remote function execute(*EciRequest request) returns byte[]|Error? =
    @java:Method {
        'class: "io.ballerina.lib.ibm.ctg.NativeClientAdaptor"
    } external;


    # Closes the IBM CTG client resources.
    # ```ballerina
    # check ctg->close();
    # ```
    # 
    # + return - A `ctg:Error` if there is an error while closing the client resources or else nil.
    isolated remote function close() returns Error? =
    @java:Method {
        'class: "io.ballerina.lib.ibm.ctg.NativeClientAdaptor"
    } external;
}
