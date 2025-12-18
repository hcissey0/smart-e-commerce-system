# Database Design Document

## Overview
The Smart E-Commerce System uses a relational database (MySQL) designed in **Third Normal Form (3NF)** to ensure data integrity and reduce redundancy.

## Entity Relationship Diagram (ERD)

```mermaid
erDiagram
    USERS ||--o{ ORDERS : places
    USERS ||--o{ REVIEWS : writes
    CATEGORIES ||--o{ PRODUCTS : contains
    PRODUCTS ||--o{ ORDER_ITEMS : "included in"
    PRODUCTS ||--o{ INVENTORY_LOGS : "tracked by"
    PRODUCTS ||--o{ REVIEWS : "reviewed in"
    ORDERS ||--o{ ORDER_ITEMS : contains

    USERS {
        int user_id PK
        string email UK
        string password_hash
        string role
    }

    CATEGORIES {
        int category_id PK
        string name UK
    }

    PRODUCTS {
        int product_id PK
        int category_id FK
        string name
        decimal price
        int stock_quantity
    }

    ORDERS {
        int order_id PK
        int user_id FK
        timestamp order_date
        decimal total_amount
    }

    ORDER_ITEMS {
        int id PK
        int order_id FK
        int product_id FK
        int quantity
        decimal price_at_purchase
    }

    INVENTORY_LOGS {
        int log_id PK
        int product_id FK
        int change_amount
        timestamp change_date
        string reason
    }

    REVIEWS {
        int review_id PK
        int product_id FK
        int user_id FK
        int rating
        text comment
        timestamp created_at
    }
```

## Schema Details

### 1. Users Table
Stores authentication and authorization details.
- **Primary Key**: `user_id`
- **Constraints**: `email` must be unique.

### 2. Categories Table
Classifies products.
- **Primary Key**: `category_id`
- **Constraints**: `name` must be unique.

### 3. Products Table
Core entity representing items for sale.
- **Primary Key**: `product_id`
- **Foreign Key**: `category_id` references `categories(category_id)`.
- **Indexes**: 
    - `idx_products_name` (for search optimization)
    - `idx_products_category` (for filtering)

### 4. Orders Table
Represents a transaction.
- **Primary Key**: `order_id`
- **Foreign Key**: `user_id` references `users(user_id)`.

### 5. OrderItems Table
Junction table for Many-to-Many relationship between Orders and Products.
- **Primary Key**: `id`
- **Foreign Keys**: 
    - `order_id` references `orders(order_id)`
    - `product_id` references `products(product_id)`
- **Note**: Stores `price_at_purchase` to preserve historical pricing.

### 6. InventoryLogs Table
Audit trail for stock adjustments.
- **Primary Key**: `log_id`
- **Foreign Key**: `product_id` references `products(product_id)`.

### 7. Reviews Table
Customer feedback.
- **Primary Key**: `review_id`
- **Foreign Keys**: 
    - `product_id` references `products(product_id)`
    - `user_id` references `users(user_id)`
- **Constraints**: `rating` between 1 and 5.

## Normalization (3NF)
- **1NF**: All columns contain atomic values.
- **2NF**: All non-key attributes are fully functional dependent on the primary key.
- **3NF**: No transitive dependencies (e.g., `category_name` is in the `categories` table, not `products`).
