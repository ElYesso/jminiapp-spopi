---
sidebar_position: 2
---

# Build Your First App

Learn how to create your first JMiniApp application in 5 minutes.

## Prerequisites

Before you begin, make sure you have:

- **Java 17 or higher**
- **Maven 3.6 or higher**

Quick check:
```bash
java -version
mvn -version
```

## Project Setup

### Create the Greeting App Project

We'll create a simple greeting application that demonstrates JMiniApp's core features. First, navigate to the JMiniApp examples directory and create the project structure:

```bash
# Navigate to the JMiniApp repository examples folder from the framework root folder
cd examples

# Generate a Maven project for the greeting app
mvn archetype:generate \
  -DgroupId=com.jminiapp.examples \
  -DartifactId=greeting \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DarchetypeVersion=1.4 \
  -DinteractiveMode=false

# Navigate into the generated project
cd greeting
```

This creates the basic structure:
```
greeting/
├── pom.xml
└── src/main/java/com/jminiapp/examples/
    └── App.java
```

:::tip Project Location
For this tutorial, we're creating the example in the `examples/` folder of the JMiniApp repository. This allows you to explore the framework alongside the counter example.

For your own applications, you can create projects anywhere outside the framework repository.
:::

### Configure the Project

 
#### 1. Update `pom.xml`

Open `pom.xml` and update it with the following configuration:

```xml
<properties>
  <maven.compiler.source>17</maven.compiler.source>
  <maven.compiler.target>17</maven.compiler.target>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

#### 2. Create the Package Structure

Delete the generated `App.java` and create the proper package structure for the greeting app:

```bash
# Remove the generated file
rm src/main/java/com/jminiapp/examples/App.java

# Remove the tests folder for now, we can add it later
rm -rv src/test

# Create the greeting package directory
mkdir -p src/main/java/com/jminiapp/examples/greeting
```

Your project is now ready for the greeting app code!

## Step 1: Add JMiniApp to Your Project

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.jminiapp</groupId>
    <artifactId>jminiapp-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

:::note
JMiniApp must be [built from source](installation#building-from-source) first, as it's not yet on Maven Central.
:::

## Step 2: Create Your State Model

Create a simple model class to represent your application state in `src/main/java/com/jminiapp/examples/greeting/Greeting.java`:

```java
package com.jminiapp.examples.greeting;

public class Greeting {
    private String message;

    public Greeting() {
        this.message = "Hello, JMiniApp!";
    }

    public Greeting(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
```

**What is this?** A plain Java class that holds your application's data. JMiniApp will automatically save and restore this between runs.

## Step 3: Create Your Application

Extend `JMiniApp` and implement the three lifecycle methods in `src/main/java/com/jminiapp/examples/greeting/GreetingApp.java`:

```java
package com.jminiapp.examples.greeting;

import com.jminiapp.core.api.*;
import java.util.*;

public class GreetingApp extends JMiniApp {
    private Greeting greeting;

    // Required constructor
    public GreetingApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        // Phase 1: Setup - Load existing data or create new
        List<Greeting> data = context.getData();
        greeting = data.isEmpty() ? new Greeting() : data.get(0);
        System.out.println("Loaded greeting: " + greeting.getMessage());
    }

    @Override
    protected void run() {
        // Phase 2: Main logic - Get user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a new greeting message: ");
        String input = scanner.nextLine();

        if (!input.trim().isEmpty()) {
            greeting.setMessage(input);
            System.out.println("Message updated!");
        }
    }

    @Override
    protected void shutdown() {
        // Phase 3: Cleanup - Save data for next run
        context.setData(List.of(greeting));
        System.out.println("Goodbye!");
    }
}
```

**Key concepts:**
- **initialize()** - Runs once at startup, loads your data
- **run()** - Your main application logic
- **shutdown()** - Saves your data before exit
- **context** - Framework service for state management

## Step 4: Bootstrap Your Application

Use `JMiniAppRunner` to configure and launch in `src/main/java/com/jminiapp/examples/greeting/GreetingAppRunner.java`:

```java
package com.jminiapp.examples.greeting;

import com.jminiapp.core.engine.JMiniAppRunner;

public class GreetingAppRunner {
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(GreetingApp.class)
            .withState(Greeting.class)
            .run(args);
    }
}
```

**What this does:**
- `forApp()` - Specifies your application class
- `withState()` - Enables state persistence for `Greeting` objects
- `run()` - Starts the lifecycle

## Step 5: Run Your Application

Compile and run:

```bash
mvn clean package
mvn exec:java -Dexec.mainClass="com.jminiapp.examples.greeting.GreetingAppRunner"
```

You should see:

```
Loaded greeting: Hello, JMiniApp!
Enter a new greeting message: Welcome to my first app!
Message updated!
Goodbye!
```

**Run it again** to see your message persisted!

## What You Just Built

Congratulations! You've created a complete JMiniApp with:

### 1. Three Lifecycle Phases

- **initialize()** - Loaded existing state or created new state
- **run()** - Executed your application logic (got user input)
- **shutdown()** - Saved state for next time

### 2. Automatic State Persistence

Your greeting is automatically saved and restored between runs. The framework handles all the details.

### 3. Type-Safe State Management

The `context.getData()` and `context.setData()` methods provide type-safe access to your application state.

---

## Adding File Persistence (Advanced)

Want to export your data to JSON files? Let's enhance the application.

### Step 1: Create a JSON Adapter

Create `src/main/java/com/jminiapp/examples/greeting/GreetingJSONAdapter.java`:

```java
package com.jminiapp.examples.greeting;

import com.jminiapp.core.adapters.JSONAdapter;

public class GreetingJSONAdapter implements JSONAdapter<Greeting> {
    @Override
    public Class<Greeting> getstateClass() {
        return Greeting.class;
    }
}
```

**What is this?** A format adapter that translates between `Greeting` objects and JSON files.

### Step 2: Register the Adapter

Update your bootstrap configuration in `GreetingAppRunner.java`:

```java
public class GreetingAppRunner {
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(GreetingApp.class)
            .named("Greeting")
            .withState(Greeting.class)
            .withAdapters(new GreetingJSONAdapter())
            .run(args);
    }
}
```

### Step 3: Add Export Functionality

Update the `shutdown()` method to export to JSON:

```java
@Override
protected void shutdown() {
    // Save state
    context.setData(List.of(greeting));

    // Export to JSON file
    try {
        context.exportData("json");
        System.out.println("Greeting exported to Greeting.json");
    } catch (Exception e) {
        System.err.println("Failed to export: " + e.getMessage());
    }

    System.out.println("Goodbye!");
}
```

### Step 4: Test Import/Export

Run the application again. It will create a `greeting.json` file in your resources directory:

```json
[
  {
    "message": "Welcome to my first app!"
  }
]
```

You can now:
- **Edit** the JSON file manually
- **Share** it with others
- **Import** it back into your app
- **Use** it with external tools

---

## Project Structure

Your complete project should look like this:

```
greeting/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── jminiapp/
        │           └── examples/
        │               └── greeting/
        │                   ├── Greeting.java           # State model
        │                   ├── GreetingApp.java        # Application class
        │                   ├── GreetingJSONAdapter.java # JSON adapter (optional)
        │                   └── GreetingAppRunner.java  # Bootstrap
        └── resources/
            └── greeting.json                           # Exported data (generated)
```

## Next Steps

Now that you have a working app, explore these topics:

- **[Common Patterns](common-patterns)** - Best practices and application patterns
- **[Lifecycle Guide](../guides/lifecycle)** - Master the application lifecycle
- **[Context API](../guides/context)** - Working with state and files
- **[Format Adapters](../guides/adapters)** - Support multiple file formats (CSV, JSON, XML)
- **[JMiniAppRunner](../guides/runner)** - Configuration options and advanced setup
- **[Templates](../examples/templates)** - Application templates and examples
