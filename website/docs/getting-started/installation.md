---
sidebar_position: 1
---

# Installation

Get JMiniApp set up in your development environment.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17 or higher**: JMiniApp requires Java 17+
- **Maven 3.6 or higher**: For building and managing dependencies

Check your installations:

```bash
java -version
mvn -version
```

## Installation

### Building from Source

:::note
JMiniApp is not yet published to Maven Central. You must build from source first to use it in your projects.
:::

Clone the repository and build the framework locally:

```bash
git clone https://github.com/jminiapp/jminiapp.git
cd jminiapp
mvn clean install
```

This will install `jminiapp-core` into your local Maven repository (`~/.m2/repository`).

### Adding JMiniApp to Your Project

After building from source, add JMiniApp as a dependency in your project's `pom.xml`:

```xml
<dependency>
    <groupId>com.jminiapp</groupId>
    <artifactId>jminiapp-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```