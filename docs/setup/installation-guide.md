# Installation Guide

## Prerequisites
Before setting up the project, ensure you have the following installed:
- **Java Development Kit (JDK)**: Version 21 or higher.
- **Apache Maven**: Version 3.8 or higher.
- **MySQL Server**: Version 8.0 or higher.
- **Git**: For version control.

## 1. Clone the Repository
Clone the project to your local machine:
```bash
git clone <repository-url>
cd smart-e-commerce-system
```

## 2. Database Setup
The application requires a MySQL database to function.

1.  **Start MySQL Server**: Ensure your MySQL service is running.
2.  **Create Database**: Log in to your MySQL shell and create the database.
    ```sql
    CREATE DATABASE ecommerce_db;
    USE ecommerce_db;
    ```
3.  **Run Schema Scripts**: Execute the SQL scripts provided in the `src/main/resources/sql` directory to create tables and insert sample data.
    ```sql
    -- Run from the project root or inside your SQL client
    SOURCE src/main/resources/sql/schema.sql;
    SOURCE src/main/resources/sql/seed_data.sql;
    ```

## 3. Configuration
You may need to configure the database connection settings to match your local environment.

1.  Navigate to `src/main/java/com/ecommerce/utils/DatabaseConnection.java`.
2.  Update the `USER` and `PASSWORD` constants:
    ```java
    private static final String URL = "jdbc:mysql://localhost:3306/ecommerce_db";
    private static final String USER = "root";      // Change if necessary
    private static final String PASSWORD = "your_password"; // Change to your MySQL password
    ```

## 4. Build the Project
Use Maven to clean the project and download dependencies:
```bash
mvn clean install
```

## 5. Run the Application
Start the JavaFX application using the Maven plugin:
```bash
mvn javafx:run
```

## Troubleshooting

### Common Issues
-   **"No suitable driver found"**: Ensure the MySQL Connector/J dependency is in your `pom.xml`.
-   **"Access denied for user"**: Double-check your username and password in `DatabaseConnection.java`.
-   **JavaFX controls not rendering**: Verify that your `module-info.java` (if present) opens the `controllers` package to `javafx.fxml`.

### Running Tests
To verify that the system is working correctly (including database connections and logic), run the unit tests:
```bash
mvn test
```
