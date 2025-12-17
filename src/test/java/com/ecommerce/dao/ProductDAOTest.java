package com.ecommerce.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.ecommerce.models.Category;
import com.ecommerce.models.Product;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductDAOTest {

  private static ProductDAO productDAO;
  private static CategoryDAO categoryDAO;
  private static Category testCategory;
  private static Product testProduct;

  @BeforeAll
  static void setUp() throws SQLException {
    productDAO = new ProductDAO();
    categoryDAO = new CategoryDAO();

    // Create a category for the products
    testCategory = new Category("Test Gadgets");
    categoryDAO.create(testCategory);
  }

  @AfterAll
  static void tearDown() throws SQLException {
    // Clean up
    if (testCategory != null) {
      categoryDAO.delete(testCategory.getCategoryId());
    }
  }

  @Test
  @Order(1)
  void testCreateProduct() throws SQLException {
    testProduct = new Product(testCategory.getCategoryId(), "Smartphone X", 999.99, 50);
    productDAO.create(testProduct);

    assertTrue(testProduct.getProductId() > 0, "Product ID should be generated");
  }

  @Test
  @Order(2)
  void testFindById() throws SQLException {
    Product retrieved = productDAO.findById(testProduct.getProductId());
    assertNotNull(retrieved, "Product should be found");
    assertEquals(testProduct.getName(), retrieved.getName());
    assertEquals(testProduct.getPrice(), retrieved.getPrice());
  }

  @Test
  @Order(3)
  void testUpdateProduct() throws SQLException {
    testProduct.setPrice(899.99);
    testProduct.setStockQuantity(45);
    productDAO.update(testProduct);

    Product retrieved = productDAO.findById(testProduct.getProductId());
    assertEquals(899.99, retrieved.getPrice(), 0.01);
    assertEquals(45, retrieved.getStockQuantity());
  }

  @Test
  @Order(4)
  void testFindByCategoryId() throws SQLException {
    List<Product> products = productDAO.findByCategoryId(testCategory.getCategoryId());
    assertFalse(products.isEmpty());
    assertTrue(products.stream().anyMatch(p -> p.getProductId() == testProduct.getProductId()));
  }

  @Test
  @Order(5)
  void testDeleteProduct() throws SQLException {
    productDAO.delete(testProduct.getProductId());
    Product retrieved = productDAO.findById(testProduct.getProductId());
    assertNull(retrieved, "Product should be deleted");
  }
}
