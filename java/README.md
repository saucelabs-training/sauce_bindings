Welcome To The New Evolution In Sauce Labs Testing.
The goal of Sauce Bindings is to make your test automation and integration in Sauce Labs, simple :)
Our fluent API helps you to understand all of the possible browser/OS combinations at design time.
You no longer need to read extensive docs, just let your IDE guide you 😉

## Quick Start with a single line of code
```
WebDriver driver = new SauceSession().start();
//now use the driver to interact with your test in Sauce Labs
driver.findElement("id").click();
```
* Defaults are Windows 10 for non-Safari
* latest Mac for Safari
## Run a test on:

### Latest Chrome
```        
SauceOptions options = new SauceOptions();
options.withChrome();
webDriver = new SauceSession(options).start();
```
### Latest Edge
```        
SauceOptions options = new SauceOptions();
options.withEdge();
webDriver = new SauceSession(options).start();
```
### Latest Firefox
```        
SauceOptions options = new SauceOptions();
options.withFirefox();
webDriver = new SauceSession(options).start();
```

### Latest Safari
```        
SauceOptions options = new SauceOptions();
options.withSafari();
webDriver = new SauceSession(options).start();
```

## If it's possible in Sauce, it's possible here:
### Run test on Linux with Firefox:
```        
SauceOptions options = new SauceOptions().
withLinux().
withFirefox();
webDriver = new SauceSession(options).start();
```
### Setting Browser versions:
```        
SauceOptions options = new SauceOptions().
withLinux().
withChrome("72");
webDriver = new SauceSession(options).start();
```
#### Edge and Safari browser versions are easy to know:
Mac OS Mojave
```        
SauceOptions options = new SauceOptions().withMacOsMojave();
webDriver = new SauceSession(options).start();
//Don't lose time trying to figure out that Mojave = "12.0"
//and that it needs to be paired with "macOS 10.14"
```
Edge version 15
```        
SauceOptions options = new SauceOptions().withEge15();
webDriver = new SauceSession(options).start();
```

## Installation

The Sauce Bindings beta are now available on MavenCentral. You can install these bindings via Maven by adding

```xml
<dependency>
    <groupId>com.saucelabs</groupId>
    <artifactId>sauce_bindings</artifactId>
    <version>1.0-beta-5</version>
</dependency>
```

to your `pom.xml` file.

## Building

Make sure you have the common Sauce Bindings [prerequisites](https://github.com/saucelabs/sauce_bindings#getting-started-and-prerequisites) set up, as well as

-  Java 9+ JDK,
-  Maven,
-  Your favorite Java IDE ([IntelliJ](https://www.jetbrains.com/idea/download/index.html) preferred but not required).

The Java Sauce Bindings are designed as a standard Maven project and follow Maven conventions.

## Local development

First clone this project, either directly or from a fork. The following instructions will be based on a local clone of the Sauce Bindings repository.

To create a `.jar` of the Java bindings in their current state in your local repository, run

```bash
mvn package
```

in the `java/` (root) directory. This will create a `.jar` file in the `java/target/` directory which you can them use as you like.

If you'd like to make Sauce Bindings available to other local projects via Maven, run

```bash
mvn install
```

in the `java/` directory. This will allow you to import Sauce Bindings as a Maven dependency in other local projects on your laptop as a snapshot of the state based on the commit that you build from. To add this reference via Maven, add

```xml
<dependency>
    <groupId>com.saucelabs</groupId>
    <artifactId>sauce_bindings</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

to other projects' `pom.xml` file to access Sauce Bindings. Note that adding these coordinates to other Maven projects will get the latest `1.0-SNAPSHOT` version that was built, and so using these coordinates are recommended for development and not for general usage.

## Testing

To execute the test suite, run

```bash
mvn test
```

Tests will also be automatically executed as part of the building process.
