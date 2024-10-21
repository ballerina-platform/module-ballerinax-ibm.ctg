## Overview

[IBM CICS Transaction Gateway (CTG)](https://www.ibm.com/products/cics-transaction-gateway) is a robust middleware solution that facilitates communication between distributed applications and IBM CICS Transaction Servers.

The `ballerinax/ibm.ctg` package provides an API to connect to an IBM CICS Transaction Gateway using Ballerina. The current connector is compatible with IBM CICS Transaction Gateway versions 9.3.

## Quickstart

To use the IBM CTG connector in your Ballerina application, modify the `.bal` file as follows:

### Step 1: Import the connector

Import `ballerinax/ibm.ctg` module into your Ballerina project.

```ballerina
import ballerinax/ibm.ctg;
```

### Step 2: Add IBM MQ driver

Add `ccf2`, `cicsjee.`, and `ctgclient` as a platform dependencies to the `Ballerina.toml`.

```toml
[[platform.java17.dependency]]
groupId = "ibm"
artifactId = "ccf2"
version = "9.3"
path = "./<path-to>/ccf2.jar"

[[platform.java17.dependency]]
groupId = "ibm"
artifactId = "cicsjee"
version = "9.3"
path = "./<path-to>/cicsjee.jar"

[[platform.java17.dependency]]
groupId = "ibm"
artifactId = "ctgclient"
version = "9.3"
path = "./<path-to>/ctgclient.jar"
```

### Step 3: Instantiate a new connector

Create an `ctg:Client` instance by giving IBM MQ configuration.

```ballerina
configurable string host = ?;
configurable int port = ?;
configurable string cicsServer = ?;
configurable string userId = ?;
configurable string password = ?;

ctg:Client ctg = check new({
    host,
    port,
    cicsServer,
    auth: {
        userId,
        password
    }
});
```

### Step 4: Invoke the connector operations

Now, utilize the available connector operations.

```ballerina
// set the CICS program name here
string programName = ...;
// set the input payload
byte[] inputPayload = ...;
byte[]? outputPayload = ctg->execute(programName = programName, commArea = inputPayload);
```

### Step 5: Run the Ballerina application

```Shell
bal run
```
