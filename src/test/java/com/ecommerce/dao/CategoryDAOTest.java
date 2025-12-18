package com.ecommerce.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.ecommerce.models.Category;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryDAOTest {

  private static CategoryDAO categoryDAO;
  private static Category testCategory;

  @BeforeAll
  static void setUp() {
    categoryDAO = new CategoryDAO();
  }

  @Test
  @Order(1)
  void testCreateCategory() throws SQLException {
    testCategory = new Category("Test Electronics");
    categoryDAO.create(testCategory);

    assertTrue(testCategory.getCategoryId() > 0, "Category ID should be generated");
  }

  @Test
  @Order(2)
  void testFindById() throws SQLException {
    Category retrieved = categoryDAO.findById(testCategory.getCategoryId());
    assertNotNull(retrieved, "Category should be found");
    assertEquals(testCategory.getName(), retrieved.getName(), "Names should match");
  }

  @Test
  @Order(3)
  void testUpdateCategory() throws SQLException {
    testCategory.setName("Updated Electronics");
    categoryDAO.update(testCategory);

    Category retrieved = categoryDAO.findById(testCategory.getCategoryId());
    assertEquals("Updated Electronics", retrieved.getName(), "Name should be updated");
  }

  @Test
  @Order(4)
  void testFindAll() throws SQLException {
    List<Category> categories = categoryDAO.findAll();
    assertFalse(categories.isEmpty(), "Should return list of categories");
    boolean found =
        categories.stream().anyMatch(c -> c.getCategoryId() == testCategory.getCategoryId());
    assertTrue(found, "Created category should be in the list");
  }

  @Test
  @Order(5)
  void testDeleteCategory() throws SQLException {
    categoryDAO.delete(testCategory.getCategoryId());
    Category retrieved = categoryDAO.findById(testCategory.getCategoryId());
    assertNull(retrieved, "Category should be deleted");
  }
}
