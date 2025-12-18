package com.ecommerce.utils;

import com.ecommerce.dao.ProductDAO;
import com.ecommerce.models.Product;
import com.ecommerce.services.ProductService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class PerformanceTestRunner {

  private static final int PRODUCT_COUNT = 10000;
  private static final String SEARCH_TERM = "Product 5000";

  public static void main(String[] args) {
    try {
      System.out.println("Starting Performance Test...");
      ProductService productService = new ProductService();
      ProductDAO productDAO = new ProductDAO();

      // 1. Populate Data
      System.out.println("Populating database with " + PRODUCT_COUNT + " products...");
      populateData();

      // 2. Test Search WITHOUT Index
      dropIndex("idx_products_name");
      long searchNoIndexTime = measureSearch(productService);
      System.out.println(
          "Search Time (No Index): " + PerformanceTimer.formatDuration(searchNoIndexTime));

      // 3. Test Search WITH Index
      createIndex("idx_products_name", "products(name)");
      long searchIndexTime = measureSearch(productService);
      System.out.println(
          "Search Time (With Index): " + PerformanceTimer.formatDuration(searchIndexTime));

      // 4. Test DB vs Cache
      // Get a valid ID first
      int testId =
          productService.getAllProducts().stream().findFirst().map(Product::getProductId).orElse(0);
      if (testId == 0) {
        System.out.println("No products found to test cache!");
        return;
      }
      System.out.println("Testing Cache with Product ID: " + testId);

      // First fetch (DB)
      PerformanceTimer timer = new PerformanceTimer();
      timer.start();
      Product p = productService.getProductById(testId);
      long dbFetchTime = timer.end();
      System.out.println("Fetch Time (DB): " + PerformanceTimer.formatDuration(dbFetchTime));

      // Second fetch (Cache)
      timer.start();
      productService.getProductById(testId);
      long cacheFetchTime = timer.end();
      System.out.println("Fetch Time (Cache): " + PerformanceTimer.formatDuration(cacheFetchTime));

      // Cleanup
      // cleanupData(); // Optional, maybe keep for manual inspection

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void populateData() throws SQLException {
    String sql =
        "INSERT INTO products (name, price, stock_quantity, category_id) VALUES (?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      conn.setAutoCommit(false); // Batch insert
      Random rand = new Random();
      for (int i = 0; i < PRODUCT_COUNT; i++) {
        stmt.setString(1, "Performance Product " + i);
        stmt.setDouble(2, 10.0 + rand.nextDouble() * 100.0);
        stmt.setInt(3, rand.nextInt(100));
        stmt.setNull(4, java.sql.Types.INTEGER);
        stmt.addBatch();

        if (i % 1000 == 0) {
          stmt.executeBatch();
        }
      }
      stmt.executeBatch();
      conn.commit();
      conn.setAutoCommit(true);
    }
  }

  private static void dropIndex(String indexName) {
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement()) {
      stmt.execute("DROP INDEX " + indexName + " ON products");
      System.out.println("Dropped Index: " + indexName);
    } catch (SQLException e) {
      System.out.println("Index might not exist or error dropping: " + e.getMessage());
    }
  }

  private static void createIndex(String indexName, String columns) {
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement()) {
      stmt.execute("CREATE INDEX " + indexName + " ON " + columns);
      System.out.println("Created Index: " + indexName);
    } catch (SQLException e) {
      System.out.println("Error creating index: " + e.getMessage());
    }
  }

  private static long measureSearch(ProductService service) throws SQLException {
    PerformanceTimer timer = new PerformanceTimer();
    timer.start();
    service.searchProductsByName(SEARCH_TERM);
    return timer.end();
  }
}
