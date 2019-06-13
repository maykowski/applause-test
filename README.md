###### Applause Test Excercise

**Requirements**

- Java 8 JDK - can be downloaded from https://download.oracle.com/otn/java/jdk/8u211-b12/478a62b7d4e34b78b671c754eaaf38ab/jdk-8u211-windows-x64.exe
- Maven 3.3.9 - can be downloaded from https://www-eu.apache.org/dist/maven/maven-3/3.6.1/binaries/apache-maven-3.6.1-bin.zip


 
**Run program**
 
 To run program on Windows you can execute `start.bat` which will also run unit test. On Unix-like system you can run following Maven command
 
 `mvn clean test exec:java -q -Dexec.mainClass=Execute -Dexec.args="country='US' device='iPhone 4 OR Galaxy S3'"`
 
 **Run Unit Tests**
 
 To run unit test only, execute  `mvn test`
 
 **Program Arguments**
 
 There two list of argument `country` and `device` that you can pass to program as key-pair values e.g.
 
 `mvn exec:java -q -Dexec.mainClass=Execute -Dexec.args="country='US' device='iPhone 4 OR Galaxy S3'"`
 
 - `country='US'` - present testers that live in US 
 - `device='iPhone 4 OR Galaxy S3'` - present testers that have `iPhone 4` or `Galaxy S3` devices
 
 Special keywords for `country` and `device`:
  - `OR` to provide more than 1 value, e.g. `country='US OR GB'`
  - `ALL` to present all result for given key, e.g. = `device='ALL'` Must not be joined with other values.