package com.ecommerce.controllers;

import com.ecommerce.models.Product;
import com.ecommerce.services.ProductService;
import com.ecommerce.utils.PerformanceTimer;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class DashboardController {

  @FXML private TextField searchField;
  @FXML private TableView<Product> productTable;
  @FXML private TableColumn<Product, Integer> colId;
  @FXML private TableColumn<Product, String> colName;
  @FXML private TableColumn<Product, Double> colPrice;
  @FXML private TableColumn<Product, Integer> colStock;
  @FXML private TableColumn<Product, Integer> colCategory;

  private ProductService productService;
  private ObservableList<Product> productList;

  public void initialize() {
    productService = new ProductService();
    productList = FXCollections.observableArrayList();

    colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    colStock.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
    colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryId"));

    productTable.setItems(productList);

    loadData();
  }

  private void loadData() {
    try {
      PerformanceTimer timer = new PerformanceTimer();
      timer.start();
      List<Product> products = productService.getAllProducts();
      long duration = timer.end();
      System.out.println("Load Data Time: " + PerformanceTimer.formatDuration(duration));

      productList.setAll(products);
    } catch (SQLException e) {
      showError("Error loading data", e.getMessage());
    }
  }

  @FXML
  private void handleSearch() {
    String query = searchField.getText();
    try {
      PerformanceTimer timer = new PerformanceTimer();
      timer.start();
      List<Product> results = productService.searchProductsByName(query);
      long duration = timer.end();
      System.out.println("Search Time: " + PerformanceTimer.formatDuration(duration));

      productList.setAll(results);
    } catch (SQLException e) {
      showError("Search Error", e.getMessage());
    }
  }

  @FXML
  private void handleSortAsc() {
    List<Product> sorted = productService.sortProductsByPrice(productList, true);
    productList.setAll(sorted);
  }

  @FXML
  private void handleSortDesc() {
    List<Product> sorted = productService.sortProductsByPrice(productList, false);
    productList.setAll(sorted);
  }

  @FXML
  private void handleRefresh() {
    loadData();
  }

  @FXML
  private void handleDelete() {
    Product selected = productTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
      try {
        productService.deleteProduct(selected.getProductId());
        productList.remove(selected);
      } catch (SQLException e) {
        showError("Delete Error", e.getMessage());
      }
    } else {
      showWarning("No Selection", "Please select a product to delete.");
    }
  }

  @FXML
  private void handleAdd() {
    // For simplicity in this iteration, we'll add a dummy product or use a simple dialog
    // Ideally, open a new Stage with a form.
    // Let's add a dummy product for now to demonstrate CRUD
    try {
      TextInputDialog dialog = new TextInputDialog("New Product");
      dialog.setTitle("Add Product");
      dialog.setHeaderText("Enter Product Name");
      dialog.setContentText("Name:");

      Optional<String> result = dialog.showAndWait();
      if (result.isPresent()) {
        Product p = new Product(1, result.get(), 100.0, 10); // Default values
        productService.createProduct(p);
        loadData();
      }
    } catch (SQLException e) {
      showError("Add Error", e.getMessage());
    }
  }

  private void showError(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setContentText(content);
    alert.showAndWait();
  }

  private void showWarning(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setContentText(content);
    alert.showAndWait();
  }
}
