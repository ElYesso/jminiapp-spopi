---
sidebar_position: 1
---

# Counter

A simple counter application demonstrating core JMiniApp concepts: lifecycle management, state persistence, and file import/export.

**Source Code:** [examples/counter](https://github.com/jminiapp/jminiapp/tree/main/examples/counter)

## Features

- **Lifecycle Management** - Initialize, run, and shutdown phases
- **State Persistence** - Counter value automatically saved between runs
- **JSON Import/Export** - Save and load counter state from files
- **Interactive Menu** - Console-based user interface

## Quick Start

```bash
cd examples/counter
mvn clean install
mvn exec:java
```

## Key Components

### State Model

Class representing the app state with the counter value:

```java
public class CounterState {
    private int value;

    public int increment() { return ++value; }
    public int decrement() { return --value; }
    public void reset() { this.value = 0; }
    public int getValue() { return value; }
}
```

### Application Lifecycle

The three-phase lifecycle in action:

```java
public class CounterApp extends JMiniApp {
    private CounterState counter;

    @Override
    protected void initialize() {
        // Load existing state or create new
        List<CounterState> data = context.getData();
        counter = data.isEmpty() ? new CounterState() : data.get(0);
    }

    @Override
    protected void run() {
        // Main application loop
        while (running) {
            displayMenu();
            handleUserInput();
        }
    }

    @Override
    protected void shutdown() {
        // Save state for next run
        context.setData(List.of(counter));
    }
}
```

### JSON Adapter

Simple adapter for JSON format support:

```java
public class CounterJSONAdapter implements JSONAdapter<CounterState> {
    @Override
    public Class<CounterState> getstateClass() {
        return CounterState.class;
    }
}
```

### Bootstrap

Configure and launch the application:

```java
public class CounterAppRunner {
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(CounterApp.class)
            .withState(CounterState.class)
            .withAdapters(new CounterJSONAdapter())
            .named("Counter")
            .run(args);
    }
}
```

## Usage Example

Interactive menu with all operations:

```
=== Counter App ===
Welcome to the Counter App!
Starting with new counter at: 0

--- Current Value: 0 ---
1. Increment (+1)
2. Decrement (-1)
3. Reset to 0
4. Export to JSON file
5. Import from JSON file
6. Exit

Choose an option: 1
Counter incremented to: 1

Choose an option: 4
Counter state exported successfully to: Counter.json
```

The exported JSON file:

```json
[
  {
    "value": 1
  }
]
```

### Editing and Importing Counter State

You can manually edit the exported JSON file and import it back:

1. **Edit the Counter.json file** - Change the value to any number you want:

```json
[
  {
    "value": 42
  }
]
```

2. **Import the modified file** - Use option 5 in the menu:

```
Choose an option: 5
Enter the file path to import: Counter.json
Counter state imported successfully from: Counter.json

--- Current Value: 42 ---
1. Increment (+1)
2. Decrement (-1)
3. Reset to 0
4. Export to JSON file
5. Import from JSON file
6. Exit
```

The counter now starts at the value you set in the JSON file (42 in this example).

## Key Concepts

This example demonstrates:

### Lifecycle Management
- **initialize()** - Load existing state or create new
- **run()** - Execute main application logic
- **shutdown()** - Save state before exit

### State Persistence
Counter value automatically persists between runs using the Context API.

### Import/Export
JSON adapter enables file-based state import/export with `context.exportData("json")` and `context.importData("json")`.

## Related

- **[Build Your First App](../getting-started/build-your-first-app)** - Step-by-step tutorial
- **[Lifecycle Guide](../guides/lifecycle)** - Understanding the application lifecycle
- **[Context API](../guides/context)** - State management in depth
- **[Format Adapters](../guides/adapters)** - Working with different file formats