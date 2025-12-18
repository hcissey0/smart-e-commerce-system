package com.ecommerce.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserSeeder {
  public static void main(String[] args) {
    try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
      String sql =
          "INSERT IGNORE INTO users (user_id, email, password_hash, role) VALUES (1,"
              + " 'admin@example.com', 'hashed_password', 'admin')";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        int rows = stmt.executeUpdate();
        if (rows > 0) {
          System.out.println("User seeded successfully.");
        } else {
          System.out.println("User already exists.");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
