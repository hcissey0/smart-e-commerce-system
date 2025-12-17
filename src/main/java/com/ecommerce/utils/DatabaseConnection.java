package com.ecommerce.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
  private static DatabaseConnection instance;
  private Connection connection;

  // TODO: Update these credentials or use environment variables
  private static final String URL = "jdbc:mysql://localhost:3306/ecommerce_db";
  private static final String USER = "root";
  private static final String PASSWORD = "password";

  private DatabaseConnection() {
    try {
      // Load driver class
      Class.forName("com.mysql.cj.jdbc.Driver");
      this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to connect to database", e);
    }
  }

  public static synchronized DatabaseConnection getInstance() {
    if (instance == null) {
      instance = new DatabaseConnection();
    } else {
      try {
        if (instance.getConnection().isClosed()) {
          instance = new DatabaseConnection();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return instance;
  }

  public Connection getConnection() {
    return connection;
  }
}
