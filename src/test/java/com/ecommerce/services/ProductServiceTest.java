package com.ecommerce.services;

import static org.junit.jupiter.api.Assertions.*;

import com.ecommerce.dao.CategoryDAO;
import com.ecommerce.models.Category;
import com.ecommerce.models.Product;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceTest {

  private static ProductService productService;
  private static CategoryDAO categoryDAO;
  private static Category testCategory;

  @BeforeAll
  static void setUp() throws SQLException {
    productService = new ProductService();
    categoryDAO = new CategoryDAO();
    testCategory = new Category("Service Test Category");
    categoryDAO.create(testCategory);
  }

  @AfterAll
  static void tearDown() throws SQLException {
    if (testCategory != null) {
      categoryDAO.delete(testCategory.getCategoryId());
    }
  }

  @Test
  @Order(1)
  void testCreateAndCache() throws SQLException {
    Product p = new Product(testCategory.getCategoryId(), "Cached Product", 100.0, 10);
    productService.createProduct(p);

    // First retrieval (should be in cache from create, or fetch from DB and cache)
    Product retrieved = productService.getProductById(p.getProductId());
    assertNotNull(retrieved);
    assertEquals("Cached Product", retrieved.getName());
  }

  @Test
  @Order(2)
  void testUpdateUpdatesCache() throws SQLException {
    // Get existing product
    List<Product> products = productService.searchProductsByName("Cached Product");
    assertFalse(products.isEmpty());
    Product p = products.get(0);

    // Update it
    p.setPrice(150.0);
    productService.updateProduct(p);

    // Retrieve again - should have new price
    Product updated = productService.getProductById(p.getProductId());
    assertEquals(150.0, updated.getPrice());
  }

  @Test
  @Order(3)
  void testSorting() throws SQLException {
    // Create more products
    productService.createProduct(
        new Product(testCategory.getCategoryId(), "Cheap Product", 10.0, 5));
    productService.createProduct(
        new Product(testCategory.getCategoryId(), "Expensive Product", 1000.0, 5));

    List<Product> all = productService.getAllProducts();

    // Sort Ascending
    List<Product> sortedAsc = productService.sortProductsByPrice(all, true);
    assertTrue(sortedAsc.get(0).getPrice() <= sortedAsc.get(1).getPrice());

    // Sort Descending
    List<Product> sortedDesc = productService.sortProductsByPrice(all, false);
    assertTrue(sortedDesc.get(0).getPrice() >= sortedDesc.get(1).getPrice());
  }
}
