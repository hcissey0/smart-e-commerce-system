package com.ecommerce.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseSetup {

    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "ecommerce_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        try {
            // 1. Connect to MySQL Server (no DB)
            System.out.println("Connecting to MySQL server...");
            try (Connection conn = DriverManager.getConnection(BASE_URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {
                
                // 2. Create Database
                System.out.println("Creating database '" + DB_NAME + "' if not exists...");
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            }

            // 3. Connect to the new Database
            System.out.println("Connecting to database '" + DB_NAME + "'...");
            try (Connection conn = DriverManager.getConnection(BASE_URL + DB_NAME, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                // 4. Read schema.sql
                System.out.println("Reading schema.sql...");
                InputStream is = DatabaseSetup.class.getResourceAsStream("/sql/schema.sql");
                if (is == null) {
                    throw new RuntimeException("schema.sql not found in resources!");
                }
                String schemaSql = new BufferedReader(new InputStreamReader(is))
                        .lines().collect(Collectors.joining("\n"));

                // 5. Execute Schema (Split by semicolon for multiple statements)
                System.out.println("Executing schema SQL...");
                String[] statements = schemaSql.split(";");
                for (String sql : statements) {
                    if (!sql.trim().isEmpty()) {
                        stmt.execute(sql);
                    }
                }
                System.out.println("Database setup completed successfully!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
