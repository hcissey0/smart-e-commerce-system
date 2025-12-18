# Smart E-Commerce System

A JavaFX Desktop Application backed by a MySQL database, demonstrating raw JDBC persistence, in-memory caching, and algorithmic optimization.

## Features
- **CRUD Operations**: Manage products and categories.
- **Search**: Efficient product search with SQL Indexing.
- **Caching**: In-memory `HashMap` cache to reduce database hits.
- **Sorting**: Sort products by price (Ascending/Descending).
- **Performance Tracking**: Real-time execution time logging.

## Prerequisites
- Java 21+
- Maven
- MySQL Server

## Setup Instructions

1.  **Database Setup**:
    - Ensure MySQL is running on `localhost:3306`.
    - Update credentials in `src/main/java/com/ecommerce/utils/DatabaseConnection.java` if necessary (Default: `root`/`root`).
    - Run the setup utility to create the database and tables:
        ```bash
        mvn compile exec:java -Dexec.mainClass="com.ecommerce.utils.DatabaseSetup"
        ```

2.  **Run the Application**:
    ```bash
    mvn javafx:run
    ```

3.  **Run Tests**:
    ```bash
    mvn test
    ```

4.  **Run Performance Test**:
    ```bash
    mvn compile exec:java -Dexec.mainClass="com.ecommerce.utils.PerformanceTestRunner"
    ```

## Performance Report
See [docs/performance-report.md](docs/performance-report.md) for detailed metrics on Indexing and Caching improvements.

## Documentation
- [Database Design](docs/database-design.md)
- [Performance Report](docs/performance-report.md)

## Architecture
- **Presentation**: JavaFX (FXML + Controllers)
- **Service**: Business Logic, Caching, Sorting
- **DAO**: JDBC Data Access
- **Database**: MySQL (3NF Schema)
