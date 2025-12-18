# Performance Report

## Overview
This report documents the performance improvements achieved by implementing Database Indexing and In-Memory Caching in the Smart E-Commerce System.

## Methodology
- **Dataset**: 10,000+ Products populated in the MySQL database.
- **Environment**: Local MySQL 8.0 instance.
- **Measurement Tool**: `System.nanoTime()` via `PerformanceTimer` utility.

## Results

### 1. Database Indexing (Search Optimization)
We compared the execution time of searching for products by name ("Product 5000") with and without an index on the `name` column.

| Scenario | Execution Time | Improvement |
| :--- | :--- | :--- |
| **Without Index** | 234.761 ms | - |
| **With Index** | 91.929 ms | **~2.5x Faster** |

**Conclusion**: Adding an index on the `name` column significantly reduces the search time by avoiding a full table scan.

### 2. Caching Strategy (Read Optimization)
We compared the time taken to retrieve a product by ID from the Database vs. the In-Memory Cache (`HashMap`).

| Scenario | Execution Time | Improvement |
| :--- | :--- | :--- |
| **Database Fetch (Cache Miss)** | 23.566 ms | - |
| **Cache Fetch (Cache Hit)** | 0.400 ms | **~59x Faster** |

**Conclusion**: The Read-Through Cache strategy provides near-instantaneous access to frequently requested data, drastically reducing database load and latency.

## Summary
The optimizations implemented (Indexing and Caching) have successfully improved the application's performance, meeting the requirements of Epic 4.
