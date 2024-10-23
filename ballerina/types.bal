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


# Represents the Client configurations for IBM CTG client.
public type ConnectionConfig record {|
    # CICS transaction gateway host
    string host;
    # CICS transaction gateway port
    int port;
    # CICS server name
    string cicsServer;
    # The timeout value (in seconds) to allow a socket to connect to a remote Gateway daemon
    int socketConnectTimeout = 15;
    # The authentication configurations for the CICS server
    Auth auth;
    # The SSL configurations for the CICS transaction gateway
    SecureSocket secureSocket?;
    # Enable application level tracing
    boolean enableTrace = false;
|};

# Represents the client authentication configuration for CICS server.
public type Auth record {|
    # The CICS userId
    string userId;
    # The CICS password
    string password;
|};

# Represents the SSL configuration for CICS transaction gateway.
public type SecureSocket record {|
    # The SSL keystore or the certificate
    string sslKeyring;
    # The `sslKeyring` password
    string sslkeyringPassword?;
    # The cipherSuites parameter can be used when establishing an SSL connection
    string[] sslCipherSuites?;
|};

# Represents the ECI request details for CICS transaction gateway.
public type EciRequest record {|
    # The program to invoke in the CICS server
    string programName;
    # The COMMAREA to be passed to the CICS program
    byte[] commArea?;
    # The size of the COMMAREA
    int commAreaSize?;
    # ECI request timeout in seconds
    int timeout = 10;
|};
