package com.ecommerce.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.ecommerce.models.Product;
import com.ecommerce.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderDAOTest {

  private OrderDAO orderDAO;
  private ProductDAO productDAO;
  private int testProductId;

  @BeforeAll
  void setup() {
    orderDAO = new OrderDAO();
    productDAO = new ProductDAO();
  }

  @BeforeEach
  void initData() throws SQLException {
    createTestUser(1);
    // Create a test product with stock 10. Use categoryId 0 to set NULL and avoid FK violation.
    Product p = new Product(0, 0, "Test Transaction Product", 100.0, 10);
    productDAO.create(p);

    // Use the ID from the created object
    testProductId = p.getProductId();
  }

  @AfterEach
  void cleanup() throws SQLException {
    try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
      // Delete order items referencing the test product
      try (PreparedStatement stmt =
          conn.prepareStatement("DELETE FROM order_items WHERE product_id = ?")) {
        stmt.setInt(1, testProductId);
        stmt.executeUpdate();
      }

      // Delete the product
      try (PreparedStatement stmt =
          conn.prepareStatement("DELETE FROM products WHERE product_id = ?")) {
        stmt.setInt(1, testProductId);
        stmt.executeUpdate();
      }

      // Delete orders for the test user
      try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM orders WHERE user_id = 1")) {
        stmt.executeUpdate();
      }

      // Delete the user
      try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE user_id = 1")) {
        stmt.executeUpdate();
      }
    }
  }

  private void createTestUser(int userId) throws SQLException {
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt =
            conn.prepareStatement(
                "INSERT IGNORE INTO users (user_id, email, password_hash, role) VALUES (?,"
                    + " 'test@example.com', 'hash', 'customer')")) {
      stmt.setInt(1, userId);
      stmt.executeUpdate();
    }
  }

  @Test
  void testPlaceOrderSuccessful() throws SQLException {
    Product p = new Product();
    p.setProductId(testProductId);
    p.setPrice(100.0);
    p.setName("Test Transaction Product");

    Map<Product, Integer> cart = new HashMap<>();
    cart.put(p, 2); // Buy 2

    boolean result = orderDAO.placeOrder(1, cart); // User ID 1 (Seed data)

    assertTrue(result);

    // Verify stock reduced
    Product updatedP = productDAO.findById(testProductId);
    assertEquals(8, updatedP.getStockQuantity());
  }

  @Test
  void testPlaceOrderInsufficientStock() {
    Product p = new Product();
    p.setProductId(testProductId);
    p.setPrice(100.0);
    p.setName("Test Transaction Product");

    Map<Product, Integer> cart = new HashMap<>();
    cart.put(p, 20); // Buy 20 (Stock is 10)

    // Should throw SQLException and Rollback
    assertThrows(
        SQLException.class,
        () -> {
          orderDAO.placeOrder(1, cart);
        });

    // Verify stock remains 10 (Rollback worked)
    try {
      Product updatedP = productDAO.findById(testProductId);
      assertEquals(10, updatedP.getStockQuantity());
    } catch (SQLException e) {
      fail("Database error during verification: " + e.getMessage());
    }
  }
}
