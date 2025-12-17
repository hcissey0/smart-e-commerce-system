package com.ecommerce.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class DatabaseConnectionTest {

  @Test
  void testConnectionSingleton() {
    DatabaseConnection instance1 = DatabaseConnection.getInstance();
    DatabaseConnection instance2 = DatabaseConnection.getInstance();

    assertNotNull(instance1, "Instance should not be null");
    assertSame(instance1, instance2, "Instances should be the same (Singleton pattern)");
  }

  @Test
  void testConnectionValidity() {
    try {
      Connection conn = DatabaseConnection.getInstance().getConnection();
      assertNotNull(conn, "Connection object should not be null");
      assertFalse(conn.isClosed(), "Connection should be open");
      assertTrue(conn.isValid(2), "Connection should be valid");
    } catch (SQLException e) {
      fail("SQL Exception occurred: " + e.getMessage());
    } catch (RuntimeException e) {
      // This might happen if the database is not running or credentials are wrong
      fail("Runtime Exception (likely connection failure): " + e.getMessage());
    }
  }
}
