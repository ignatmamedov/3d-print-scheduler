# 3D Print Scheduler

![Java](https://img.shields.io/badge/Java-17+-blue)
![License](https://img.shields.io/badge/license-MIT-green)

An intelligent 3D print queue management system designed to optimize production workflow in print farms by minimizing spool changes and maximizing printer efficiency.

## Table of Contents

- [About](#about)
- [Features](#features)
- [Architecture](#architecture)
- [Design Patterns](#design-patterns)
- [Project Structure](#project-structure)
- [Installation & Setup](#installation--setup)
- [Usage](#usage)
- [Diagrams](#diagrams)
- [Technologies](#technologies)

## About

**3D Print Scheduler** is a queue management system for 3D printing that optimizes the production process through intelligent task distribution among printers, taking into account material availability (spools) and printer types.

### The Problem

Changing a spool (filament roll) is a time-intensive operation. When multiple orders use the same color and material type, it's far more efficient to print them sequentially, minimizing the number of spool changes.

### The Solution

The program doesn't control printers directly, but instead:
- Tracks all available print jobs
- Selects the most optimal job for each printer
- Considers current materials and their availability
- Minimizes the number of spool changes
- Provides operators with a prioritized list of jobs to start

## Features

### Core Functionality
- ✅ 3D print queue management
- ✅ Intelligent task distribution across printers
- ✅ Printer status tracking (idle, busy, completed, error)
- ✅ Spool management with material type and color tracking
- ✅ Support for multiple printing strategies
- ✅ Performance monitoring (Dashboard)
- ✅ CSV and JSON data file support

### Printer Types
- **StandardFDM** — Standard FDM printer with single spool
- **MultiColor** — Multi-color printer (up to 4 simultaneous spools)
- **HousedPrinter** — Enclosed printer (inherits from StandardFDM)

### Printing Strategies
1. **BasePrintingStrategy** — Basic task selection strategy
2. **EfficientSpoolChange** — Strategy with efficient spool changes
3. **LessSpoolChanges** — Strategy minimizing spool changes

### Filament Types
- **PLA** — Polylactic Acid
- **PETG** — Polyethylene Terephthalate Glycol
- **ABS** — Acrylonitrile Butadiene Styrene

## Architecture

The project is built using modern object-oriented design principles and architectural patterns.

### Core Components

```
┌─────────────────┐
│      Main       │  ← Entry point
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│     Facade      │  ← Unified interface
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│  PrintManager   │  ← Core business logic
└────┬───┬───┬────┘
     │   │   │
     ↓   ↓   ↓
┌────────┐ ┌────────┐ ┌────────┐
│Handler │ │Strategy│ │Observer│
└────────┘ └────────┘ └────────┘
```

### Application Layers

1. **Presentation Layer**
   - `Main.java` — Main application class
   - `View` — Display interface
   - `TerminalView` — Console output implementation
   - `MenuPrinter` — Menu display

2. **Application Layer**
   - `Facade.java` — Simplified interface for system interaction
   - `PrintManager.java` — Business logic coordinator

3. **Domain Layer**
   - `models/` — Data models (Print, PrintTask, Spool)
   - `printers/` — Printer types (Printer, StandardFDM, MultiColor, HousedPrinter)
   - `types/` — Enumerations (FilamentType)

4. **Infrastructure Layer**
   - `dataprovider/` — Data handling (CSV, JSON)
   - `handlers/` — Handlers for printers, tasks, and spools
  - `strategy/` — Printing strategy implementations
  - `observer/` — Observer pattern implementation

## Design Patterns

### 1. Facade Pattern
**Purpose:** Provides a simplified interface to a complex subsystem.**Implementation:** The `Facade` class hides the complexity of interacting with `PrintManager`, `Dashboard`, `MenuPrinter`, and other components.

```java
public class Facade {
    private final PrintManager printManager;
    private final Dashboard dashboard;
    private final MenuPrinter menuPrinter;
    
    public String startPrintQueue() {
        return printManager.startPrintQueue();
    }
}
```

### 2. Strategy Pattern
**Purpose:** Defines a family of algorithms, encapsulates each one, and makes them interchangeable.

**Implementation:** Different printing strategies (`BasePrintingStrategy`, `EfficientSpoolChange`, `LessSpoolChanges`) implement the `PrintingStrategy` interface.

```java
public interface PrintingStrategy {
    String selectPrintTask(Printer printer, List<PrintTask> tasks, 
                          List<Printer> printers, List<Spool> freeSpools);
}
```

### 3. Observer Pattern
**Purpose:** Defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified.

**Implementation:** `Dashboard` observes `PrintManager` and receives notifications about print events.

```java
public interface Observer {
    void update(PrintEvent event);
}

public class Dashboard implements Observer {
    @Override
    public void update(PrintEvent event) {
        this.spoolChangeCount = event.getSpoolChangeCount();
        this.printsFulfilled = event.getPrintsFulfilled();
    }
}
```

### 4. Adapter Pattern
**Purpose:** Allows objects with incompatible interfaces to work together.

**Implementation:** `CsvAdapter` and `JsonAdapter` adapt different data formats to the unified `SourceAdapter` interface.

```java
public interface SourceAdapter {
    // Unified interface for reading data
}

public class CsvAdapter implements SourceAdapter { }
public class JsonAdapter implements SourceAdapter { }
```

### 5. Factory Pattern
**Purpose:** Defines an interface for creating objects, allowing subclasses to alter the type of objects created.

**Implementation:** `PrinterFactory` creates different types of printers.

```java
public class PrinterFactory {
    public Printer createPrinter(String type, int id, String name) {
        // Printer creation logic
    }
}
```

### 6. Data Transfer Object (DTO)
**Purpose:** Transfer data between application subsystems.

**Implementation:** DTO classes (`PrintDTO`, `PrinterDTO`, `PrintTaskDTO`, `SpoolDTO`) for safe data transfer between layers.

### 7. Handler Pattern
**Purpose:** Encapsulates processing logic for specific object types.

**Implementation:** `PrinterHandler`, `PrintTaskHandler`, `SpoolHandler` manage their respective objects.

## Project Structure

```
72/
├── cleancode/                    # Main codebase
│   └── src/
│       └── saxion/
│           ├── Main.java         # Entry point
│           ├── Dashboard.java    # Statistics monitoring
│           ├── PrintManager.java # Core business logic
│           ├── dataprovider/     # Data handling
│           │   ├── DataProvider.java
│           │   ├── FileProvider.java
│           │   └── reader/
│           │       ├── CsvAdapter.java
│           │       ├── JsonAdapter.java
│           │       ├── Mapper.java
│           │       └── SourceAdapter.java
│           ├── facade/           # Facade and DTOs
│           │   ├── Facade.java
│           │   ├── PrintDTO.java
│           │   ├── PrinterDTO.java
│           │   ├── PrintTaskDTO.java
│           │   ├── SpoolDTO.java
│           │   └── StandardFDMDTO.java
│           ├── handlers/         # Object handlers
│           │   ├── PrinterHandler.java
│           │   ├── PrintTaskHandler.java
│           │   └── SpoolHandler.java
│           ├── input/            # Input handling
│           │   ├── ConsoleInput.java
│           │   └── UserInput.java
│           ├── menu/             # Menu system
│           │   └── MenuPrinter.java
│           ├── models/           # Domain models
│           │   ├── Print.java
│           │   ├── PrintTask.java
│           │   └── Spool.java
│           ├── observer/         # Observer pattern
│           │   ├── Observable.java
│           │   ├── Observer.java
│           │   └── PrintEvent.java
│           ├── printers/         # Printer types
│           │   ├── Printer.java
│           │   ├── StandardFDM.java
│           │   ├── MultiColor.java
│           │   ├── PrinterFactory.java
│           │   └── SpoolManager.java
│           ├── strategy/         # Printing strategies
│           │   ├── PrintingStrategy.java
│           │   ├── BasePrintingStrategy.java
│           │   ├── EfficientSpoolChange.java
│           │   └── LessSpoolChanges.java
│           ├── types/            # Enumerations
│           │   └── FilamentType.java
│           └── view/             # Views
│               ├── View.java
│               └── TerminalView.java
├── src/main/resources/           # Data files
│   ├── printers.csv
│   ├── printers.json
│   ├── prints.csv
│   ├── prints.json
│   ├── spools.csv
│   └── spools.json
├── docs/                         # Documentation
│   ├── activity.puml             # Activity diagram
│   ├── class_fixed.puml          # Class diagram
│   ├── seq_order.puml            # Sequence diagram (order)
│   ├── seq_print.puml            # Sequence diagram (print)
│   └── structural_problem_1.puml # Structural diagram
└── tests/                        # Tests
    ├── MainTest.java
    ├── FakeUserInput.java
    └── testing.md
```

## Installation & Setup

### Requirements
- Java 17+
- Maven (optional)

### Running the Application

1. **Clone the repository:**
```bash
git clone https://github.com/ignatmamedov/3d-print-scheduler.git
cd 3d-print-scheduler
```

2. **Compile the project:**
```bash
javac -d bin -sourcepath cleancode/src cleancode/src/saxion/Main.java
```

3. **Run the application:**
```bash
java -cp bin saxion.Main src/main/resources/prints.csv src/main/resources/spools.csv src/main/resources/printers.csv
```

Or with JSON files:
```bash
java -cp bin saxion.Main src/main/resources/prints.json src/main/resources/spools.json src/main/resources/printers.json
```

## Usage

### Main Menu

After launching the application, you'll see the following menu:

```
========== MENU ==========
1. Add new print task
2. Register print completion
3. Register printer failure
4. Change print strategy
5. Start print queue
6. Show prints
7. Show printers
8. Show spools
9. Show pending print tasks
10. Show dashboard stats
0. Exit
==========================
```

### Main Operations

#### 1. Adding a New Task
- Select option `1`
- Choose a print from the list
- Select filament type (PLA/PETG/ABS)
- Choose colors for each layer of the print

#### 2. Starting the Print Queue
- Select option `5`
- The system automatically distributes tasks among available printers

#### 3. Registering Print Completion
- Select option `2`
- Enter the ID of the printer that completed the print
- The system automatically assigns a new task if available

#### 4. Changing Strategy
- Select option `4`
- Choose one of the available strategies:
  - Base Printing Strategy
  - Efficient Spool Change
  - Less Spool Changes

#### 5. Viewing Statistics
- Select option `10`
- See the number of spool changes and completed prints

### Keyboard Control

The entire interface can be controlled using only the numeric keypad (numpad), which speeds up operation when managing multiple printers.

## Diagrams

The project includes several UML diagrams located in the `docs/` folder:

### 1. Activity Diagram (`activity.puml`)
Shows the complete process from customer order placement to finished print delivery, including interactions between:
- Customer
- Webserver
- Deventer Server
- Print Farm Server
- 3D Scheduler
- Computers with USB ports
- Printers
- Employees

### 2. Class Diagram (`class_fixed.puml`)
Displays the structure of main classes and their relationships:
- Printer hierarchy (Printer → StandardFDM → HousedPrinter/MultiColor)
- Interaction with models (Print, PrintTask, Spool)
- FilamentType usage

### 3. Sequence Diagrams
- **seq_order.puml** — Order placement process
- **seq_print.puml** — Print job execution process

### 4. Structural Diagram (`structural_problem_1.puml`)
Details the structure of printer classes with methods and properties.

## Technologies

- **Programming Language:** Java 17+
- **Design Patterns:** Facade, Strategy, Observer, Adapter, Factory, DTO, Handler
- **Principles:** SOLID, Clean Code, OOP
- **Data Formats:** CSV, JSON
- **Documentation:** PlantUML (UML diagrams)
- **Architecture:** Layered Architecture

## Integration Workflow

The system is part of a larger print farm infrastructure:

```
Customer → Webserver → Deventer Server → Print Farm → 3D Scheduler
                                              ↓
                                    Computers with USB
                                              ↓
                                         Printers
```

## Project Goals

1. **Production Optimization** — Minimize downtime and material changes
2. **Flexibility** — Support various printer types and strategies
3. **Extensibility** — Easy addition of new printer types and strategies
4. **Usability** — Simple interface for quick operator workflow
5. **Monitoring** — Track efficiency and statistics

## Workflow

### Standard Operating Procedure:

1. **Add Orders** — All orders are added to the queue
2. **Start Queue** — System distributes tasks among printers
3. **Get List** — Operator sees which tasks are assigned to which printers
4. **Physical Start** — Operator starts printers with corresponding tasks
5. **Print Completion** — Operator notifies system of completion
6. **Automatic Selection** — System automatically selects the next task
7. **Repeat** — Process repeats

### Advantages of This Approach:

- **Intelligent Selection** — Tasks selected considering available materials
- **Minimize Downtime** — Printers constantly loaded with work
- **Fewer Spool Changes** — Grouping tasks by colors and materials
- **Transparency** — Complete information about system state
- **Speed** — Control using numeric keypad only

## Usage Examples

### Example 1: Adding a Multi-Color Print Task

```
1. Select: 1 (Add new print task)
2. Choose print: 3 (Dragon Model)
3. Select filament type: 1 (PLA)
4. Choose color for layer 1: 2 (Red)
5. Choose color for layer 2: 5 (Blue)
✅ Task added to queue
```

### Example 2: Changing Strategy to Minimize Spool Changes

```
1. Select: 4 (Change print strategy)
2. Available strategies:
   1. Base Printing Strategy
   2. Efficient Spool Change
   3. Less Spool Changes
3. Choose: 3
✅ Strategy changed to "Less Spool Changes"
```

### Example 3: Viewing Statistics

```
Select: 10 (Show dashboard stats)

==================== DASHBOARD ====================
Spool changes: 15
Prints fulfilled: 47
===================================================
```