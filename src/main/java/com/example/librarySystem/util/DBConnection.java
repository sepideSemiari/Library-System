package com.example.librarySystem.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;
    private DBConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");
            PreparedStatement pstm = connection.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name IN ('book_detail', 'borrow_table', 'member_detail', 'return_detail')");
            ResultSet resultSet = pstm.executeQuery();
            if (!resultSet.next()) {
                String sql = "\n" +
                        "CREATE TABLE book_detail (\n" +
                        " id varchar(10) NOT NULL PRIMARY KEY ,\n" +
                        " title varchar(100) DEFAULT null,\n" +
                        " author varchar(100) DEFAULT NULL,\n" +
                        " status varchar(100) \n" +
                        ");\n" +
                        "\n" +
                        "CREATE TABLE member_detail (\n" +
                        " id varchar(10) PRIMARY KEY ,\n" +
                        " name varchar(100) NOT NULL,\n" +
                        " address varchar(100) Default NULL,\n" +
                        " contact varchar(11) Default NULL\n" +
                        ");\n" +
                        "\n" +
                        "CREATE TABLE borrow_detail (\n" +
                        " borrowId varchar(10) primary key ,\n" +
                        " date date ,\n" +
                        " memberId varchar ,\n" +
                        " bookId varchar ,\n" +
                        " CONSTRAINT fk_member FOREIGN KEY (memberId) REFERENCES member_detail (id),\n" +
                        " CONSTRAINT fk_book FOREIGN KEY (bookId) REFERENCES book_detail (id)\n" +
                        ");\n" +
                        "\n" +
                        "CREATE TABLE return_detail (\n" +
                        " returnId varchar not null primary key ,\n" +
                        " borrowDate date NOT NULL,\n" +
                        " returnedDate date DEFAULT NULL,\n" +
                        " fine int DEFAULT NULL,\n" +
                        " borrowId varchar,\n" +
                        " CONSTRAINT fk_borrow FOREIGN KEY (borrowId) REFERENCES borrow_detail (borrowId)" +
                        ");\n";
                pstm = connection.prepareStatement(sql);
                pstm.execute();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static DBConnection getInstance() {
        return dbConnection = ((dbConnection == null) ? new DBConnection() : dbConnection);
    }
    public Connection getConnection() {
        return connection;
    }
}