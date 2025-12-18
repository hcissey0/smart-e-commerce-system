# Application Architecture

## Overview
The Smart E-Commerce System is a JavaFX desktop application built using a layered architecture to ensure separation of concerns, maintainability, and scalability. It connects to a relational database (MySQL) using raw JDBC for persistence.

## Architectural Layers

### 1. Presentation Layer (UI)
- **Technology**: JavaFX (FXML + Controllers).
- **Responsibility**: Handles user interactions, displays data, and captures input.
- **Components**:
    - `*.fxml`: XML-based UI layout definitions.
    - `*Controller.java`: Java classes that bind UI elements to logic (e.g., `ProductListController`, `DashboardController`).

### 2. Service Layer (Business Logic)
- **Responsibility**: Orchestrates business rules, transactions, and optimization logic.
- **Key Features**:
    - **Caching**: Implements a Read-Through cache using `HashMap` to reduce database hits.
    - **Algorithms**: Handles in-memory sorting and filtering of data.
- **Components**:
    - `ProductService.java`: Manages product-related logic and caching.
    - `OrderService.java`: Handles order processing.

### 3. Data Access Layer (DAO)
- **Pattern**: Data Access Object (DAO).
- **Responsibility**: Abstraction layer for direct database interactions.
- **Technology**: JDBC (Java Database Connectivity).
- **Key Features**:
    - Uses `PreparedStatement` to prevent SQL injection.
    - Maps SQL `ResultSet` to Java POJOs.
- **Components**:
    - `ProductDAO.java`: CRUD operations for products.
    - `UserDAO.java`: User authentication and management.

### 4. Database Layer
- **Technology**: MySQL.
- **Responsibility**: Persistent storage of data in a normalized (3NF) schema.
- **Optimization**: Uses indexes on high-lookup columns for performance.

## Data Flow
1.  **User Action**: User clicks "Search" in the UI.
2.  **Controller**: `ProductListController` captures the input and calls `ProductService.searchProducts()`.
3.  **Service**:
    -   Checks the **Cache** first.
    -   If not found, calls `ProductDAO.findByName()`.
4.  **DAO**: Executes `SELECT * FROM products WHERE name LIKE ?` using JDBC.
5.  **Database**: Returns the result set.
6.  **Return Path**: Data is mapped to `Product` objects, cached in the Service, and returned to the Controller to update the `TableView`.

## Directory Structure
```text
src/main/java/com/ecommerce/
├── app/            # Main entry point
├── controllers/    # JavaFX Controllers
├── dao/            # Data Access Objects (JDBC)
├── models/         # Domain POJOs
├── services/       # Business Logic & Caching
└── utils/          # Database Connection & Helpers
```
