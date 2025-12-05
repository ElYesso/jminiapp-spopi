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

Create a simple model class to represent your application state:

```java
package com.example.hello;

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

Extend `JMiniApp` and implement the three lifecycle methods:

```java
package com.example.hello;

import com.jminiapp.core.api.*;
import java.util.*;

public class HelloApp extends JMiniApp {
    private Greeting greeting;

    // Required constructor
    public HelloApp(JMiniAppConfig config) {
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

Use `JMiniAppRunner` to configure and launch:

```java
package com.example.hello;

import com.jminiapp.core.engine.JMiniAppRunner;

public class Main {
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(HelloApp.class)
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
mvn exec:java -Dexec.mainClass="com.example.hello.Main"
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

```java
package com.example.hello;

import com.jminiapp.core.adapters.JSONAdapter;

public class GreetingJSONAdapter extends JSONAdapter<Greeting> {
    public GreetingJSONAdapter() {
        super(Greeting.class);
    }
}
```

**What is this?** A format adapter that translates between `Greeting` objects and JSON files.

### Step 2: Register the Adapter

Update your bootstrap configuration:

```java
public class Main {
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(HelloApp.class)
            .withState(Greeting.class)
            .withAdapters(new GreetingJSONAdapter())  // ← Register adapter
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
        context.exportData("greeting.json", "json");
        System.out.println("Greeting exported to greeting.json");
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
my-app/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── hello/
        │               ├── Greeting.java           # State model
        │               ├── HelloApp.java           # Application class
        │               ├── GreetingJSONAdapter.java # JSON adapter (optional)
        │               └── Main.java               # Bootstrap
        └── resources/
            └── greeting.json                       # Exported data (generated)
```

## Next Steps

Now that you have a working app, explore these topics:

- **[Common Patterns](common-patterns)** - Best practices and application patterns
- **[Lifecycle Guide](../guides/lifecycle)** - Master the application lifecycle
- **[Context API](../guides/context)** - Working with state and files
- **[Format Adapters](../guides/adapters)** - Support multiple file formats (CSV, JSON, XML)
- **[JMiniAppRunner](../guides/runner)** - Configuration options and advanced setup
- **[Templates](../examples/templates)** - Application templates and examples
