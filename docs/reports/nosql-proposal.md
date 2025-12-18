# NoSQL Integration Proposal: Product Reviews

## Objective
To explore the benefits of using a NoSQL database (e.g., MongoDB) for storing customer reviews, as outlined in Epic 4.2.

## Problem Statement
In a traditional Relational Database (SQL), storing reviews can be rigid. Reviews often contain unstructured text, varying metadata (images, tags), and can grow rapidly in volume. A strict schema might limit the flexibility of user-generated content.

## Proposed Solution: Document Store (NoSQL)
We propose using a **Document-Oriented Database** (like MongoDB) to store reviews as JSON documents.

### JSON Schema Design
Unlike the rigid rows in MySQL, a review document can be flexible:

```json
{
  "_id": "review_12345",
  "product_id": 101,
  "user_id": 55,
  "rating": 5,
  "title": "Great Product!",
  "comment": "I loved the build quality. It arrived on time.",
  "timestamp": "2023-10-27T10:00:00Z",
  "verified_purchase": true,
  "helpful_votes": 12,
  "images": [
    "url_to_image1.jpg",
    "url_to_image2.jpg"
  ],
  "tags": ["quality", "fast-shipping"]
}
```

## Justification (Why NoSQL?)

1.  **Schema Flexibility**:
    -   Users might add photos, tags, or detailed pros/cons lists. NoSQL allows us to store this varying structure without altering the database schema (ALTER TABLE).

2.  **Scalability**:
    -   Reviews are "write-heavy" and "read-heavy". NoSQL databases are designed to scale horizontally (sharding) more easily than relational databases to handle millions of reviews.

3.  **Performance**:
    -   Retrieving a single product's reviews is a simple key-value lookup or a query on `product_id`. We can fetch the entire document (including nested arrays like images) in one go, avoiding complex SQL JOINs.

## Integration Strategy
-   **Hybrid Approach**: Keep core transactional data (Users, Orders, Inventory) in **MySQL** (ACID compliance is critical there).
-   **Microservice/Module**: Offload the "Reviews" feature to a separate module that connects to the NoSQL store.
