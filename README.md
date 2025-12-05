# JMiniApp

A lightweight educational Java framework for building standalone mini applications with seamless and flexible state persistence using multiple file formats.

## What is JMiniApp?

JMiniApp simplifies the development of standalone mini applications by providing:

- **Seamless State Persistence** - Automatic state management with file-based storage
- **Multi-Format Support** - Built-in support for JSON, CSV, and custom formats
- **Clean Lifecycle Management** - Well-defined phases (initialize, run, shutdown)
- **Extensible Architecture** - Easy-to-implement adapters for custom data formats
- **Zero External Dependencies** - Minimal footprint for core functionality

## Quick Example

```java
public class HelloApp extends JMiniApp {
    private Greeting greeting;

    public HelloApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        List<Greeting> data = context.getData();
        greeting = data.isEmpty() ? new Greeting() : data.get(0);
    }

    @Override
    protected void run() {
        // Your application logic here
    }

    @Override
    protected void shutdown() {
        context.setData(List.of(greeting));
    }

    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(HelloApp.class)
            .withState(Greeting.class)
            .run(args);
    }
}
```

## Documentation

For complete documentation, tutorials, and guides, see the [website](./website) folder:

- [Installation](https://jminiapp.dev/docs/getting-started/installation) - Set up JMiniApp in your project
- [Build Your First App](https://jminiapp.dev/docs/getting-started/build-your-first-app) - Create your first JMiniApp in 5 minutes
- [Guides](https://jminiapp.dev/docs/guides/lifecycle) - Framework concepts and best practices

## Example Application

Check out the **[Counter App](./examples/counter)** - a complete example demonstrating:
- State management with counter increments/decrements
- JSON persistence for saving counter state
- Clean application lifecycle implementation
- Import/export functionality

## Project Structure

```
jminiapp/
├── modules/          # Framework core modules
├── examples/         # Example applications
│   └── counter/      # Counter app example
└── website/          # Documentation website (Docusaurus)
```

## Building from Source

```bash
# Build the framework
mvn clean install

# Run tests
mvn test

# Build documentation website
cd website
npm install
npm run build
```

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## License

This project is an educational framework designed for learning Java application architecture and design patterns.

---

**Get Started:** [jminiapp.dev](https://jminiapp.dev)
