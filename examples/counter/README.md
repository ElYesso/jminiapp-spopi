# Counter Example

A simple counter application demonstrating the JMiniApp framework.

## Overview

This example shows how to create a basic mini-app using JMiniApp core that manages a single counter value. Users can increment, decrement, and reset the counter through an interactive menu.

## Features

- **Increment**: Add 1 to the counter
- **Decrement**: Subtract 1 from the counter
- **Reset**: Set the counter back to 0
- **Export to JSON**: Save the current counter state to a JSON file
- **Import from JSON**: Load counter state from a JSON file
- **Persistent State**: Counter value is maintained in the app state

## Project Structure

```
counter/
├── pom.xml
├── README.md
└── src/main/java/com/jminiapp/examples/counter/
    ├── CounterApp.java          # Main application class
    ├── CounterAppRunner.java    # Bootstrap configuration
    ├── CounterState.java        # Counter model
    └── CounterJSONAdapter.java  # JSON format adapter
```

## Key Components

### CounterState
A simple model class that represents the counter value with methods to:
- `increment()`: Increase the value by 1
- `decrement()`: Decrease the value by 1
- `reset()`: Reset to 0

### CounterJSONAdapter
A format adapter that enables JSON import/export for `CounterState`:
- Implements `JSONAdapter<CounterState>` from the framework
- Registers with the framework during app bootstrap
- Provides automatic serialization/deserialization

### CounterApp
The main application class that extends `JMiniApp` and implements:
- `initialize()`: Set up the app and load existing counter state
- `run()`: Main loop displaying menu and handling user input
- `shutdown()`: Save the counter state before exiting
- Uses framework's `context.importData()` and `context.exportData()` for file operations

### CounterAppRunner
Bootstrap configuration that:
- Registers the `CounterJSONAdapter` with `.withAdapters()`
- Configures the app name and model class
- Launches the application

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build the project

From the **project root** (not the examples/counter directory):
```bash
mvn clean install
```

This will build both the jminiapp-core module and the counter example.

### Run the application

Option 1: Using Maven exec plugin (from the examples/counter directory)
```bash
cd examples/counter
mvn exec:java
```

Option 2: Using the packaged JAR (from the examples/counter directory)
```bash
cd examples/counter
java -jar target/counter-app.jar
```

Option 3: From the project root
```bash
cd examples/counter && mvn exec:java
```

## Usage Example

### Basic Operations

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

--- Current Value: 1 ---
1. Increment (+1)
2. Decrement (-1)
3. Reset to 0
4. Export to JSON file
5. Import from JSON file
6. Exit

Choose an option: 1
Counter incremented to: 2

--- Current Value: 2 ---
1. Increment (+1)
2. Decrement (-1)
3. Reset to 0
4. Export to JSON file
5. Import from JSON file
6. Exit

Choose an option: 2
Counter decremented to: 1
```

### Export to JSON

```
--- Current Value: 5 ---
1. Increment (+1)
2. Decrement (-1)
3. Reset to 0
4. Export to JSON file
5. Import from JSON file
6. Exit

Choose an option: 4
Enter filename to export (e.g., Counter.json): my-counter.json
Counter state exported successfully to: /path/to/my-counter.json
```

The exported JSON file will look like:
```json
[
  {
    "value": 5
  }
]
```

### Import from JSON

```
--- Current Value: 0 ---
1. Increment (+1)
2. Decrement (-1)
3. Reset to 0
4. Export to JSON file
5. Import from JSON file
6. Exit

Choose an option: 5
Enter filename to import (e.g., Counter.json): my-counter.json
Counter state imported successfully! New value: 5
```

## Next Steps

Try extending this example by:
- Adding custom increment/decrement amounts
- Implementing undo/redo functionality
- Adding multiple counters
- Adding CSV export functionality using the `CSVAdapter`
- Adding validation to prevent invalid JSON imports
- Implementing auto-save functionality
