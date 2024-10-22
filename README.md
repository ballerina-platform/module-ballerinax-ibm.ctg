# Ballerina IBM CICS Transaction Gateway connector

[![Build](https://github.com/ballerina-platform/module-ballerinax-ibm.ctg/actions/workflows/ci.yml/badge.svg)](https://github.com/ballerina-platform/module-ballerinax-ibm.ctg/actions/workflows/ci.yml)
[![Trivy](https://github.com/ballerina-platform/module-ballerinax-ibm.ctg/actions/workflows/trivy-scan.yml/badge.svg)](https://github.com/ballerina-platform/module-ballerinax-ibm.ctg/actions/workflows/trivy-scan.yml)
[![GraalVM Check](https://github.com/ballerina-platform/module-ballerinax-ibm.ctg/actions/workflows/build-with-bal-test-graalvm.yml/badge.svg)](https://github.com/ballerina-platform/module-ballerinax-ibm.ctg/actions/workflows/build-with-bal-test-graalvm.yml)
[![GitHub Last Commit](https://img.shields.io/github/last-commit/ballerina-platform/module-ballerinax-ibm.ctg.svg)](https://github.com/ballerina-platform/module-ballerinax-ibm.ctg/commits/main)
[![GitHub Issues](https://img.shields.io/github/issues/ballerina-platform/ballerina-library/module/ibm.ctg.svg?label=Open%20Issues)](https://github.com/ballerina-platform/ballerina-library/labels/module%2Fibm.ctg)

[IBM CICS Transaction Gateway (CTG)](https://www.ibm.com/products/cics-transaction-gateway) is a robust middleware solution that facilitates communication between distributed applications and IBM CICS Transaction Servers.

The `ballerinax/ibm.ctg` package provides an API to connect to an IBM CICS Transaction Gateway using Ballerina. The current connector is compatible with IBM CICS Transaction Gateway version 9.3.

## Quickstart

To use the IBM CTG connector in your Ballerina application, modify the `.bal` file as follows:

### Step 1: Import the connector

Import `ballerinax/ibm.ctg` module into your Ballerina project.

```ballerina
import ballerinax/ibm.ctg;
```

### Step 2: Add IBM MQ driver

Add `ccf2`, `cicsjee`, and `ctgclient` as platform dependencies to the `Ballerina.toml`.

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

```shell
bal run
```

## Build from the source

### Setting up the prerequisites

1. Download and install Java SE Development Kit (JDK) version 17. You can download it from either of the following sources:

    * [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
    * [OpenJDK](https://adoptium.net/)

   > **Note:** After installation, remember to set the `JAVA_HOME` environment variable to the directory where JDK was installed.

2. Download and install [Ballerina Swan Lake](https://ballerina.io/).

3. Download and install [Docker](https://www.docker.com/get-started).

   > **Note**: Ensure that the Docker daemon is running before executing any tests.

4. Export Github Personal access token with read package permissions as follows,

    ```bash
    export packageUser=<Username>
    export packagePAT=<Personal access token>
    ```

### Build options

Execute the commands below to build from the source.

1. To build the package:

   ```bash
   ./gradlew clean build
   ```

2. To run the tests:

   ```bash
   ./gradlew clean test
   ```

3. To build the without the tests:

   ```bash
   ./gradlew clean build -x test
   ```

4. To run tests against different environments:

   ```bash
   ./gradlew clean test -Pgroups=<Comma separated groups/test cases>
   ```

5. To debug the package with a remote debugger:

   ```bash
   ./gradlew clean build -Pdebug=<port>
   ```

6. To debug with the Ballerina language:

   ```bash
   ./gradlew clean build -PbalJavaDebug=<port>
   ```

7. Publish the generated artifacts to the local Ballerina Central repository:

    ```bash
    ./gradlew clean build -PpublishToLocalCentral=true
    ```

8. Publish the generated artifacts to the Ballerina Central repository:

   ```bash
   ./gradlew clean build -PpublishToCentral=true
   ```

## Contribute to Ballerina

As an open-source project, Ballerina welcomes contributions from the community.

For more information, go to the [contribution guidelines](https://github.com/ballerina-platform/ballerina-lang/blob/master/CONTRIBUTING.md).

## Code of conduct

All the contributors are encouraged to read the [Ballerina Code of Conduct](https://ballerina.io/code-of-conduct).

## Useful links

* For more information go to the [`ibm.ctg` package](https://central.ballerina.io/ballerinax/ibm.ctg/latest).
* For example demonstrations of the usage, go to [Ballerina By Examples](https://ballerina.io/learn/by-example/).
* Chat live with us via our [Discord server](https://discord.gg/ballerinalang).
* Post all technical questions on Stack Overflow with the [#ballerina](https://stackoverflow.com/questions/tagged/ballerina) tag.
