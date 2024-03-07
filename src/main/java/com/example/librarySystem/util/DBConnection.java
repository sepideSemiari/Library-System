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
            PreparedStatement pstm = connection.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name IN ('bookdetail', 'issuetb', 'memberdetail', 'returndetail')");
            ResultSet resultSet = pstm.executeQuery();
            if (!resultSet.next()) {
                String sql = "\n" +
                        "CREATE TABLE book_detail (\n" +
                        "  id varchar(10) NOT NULL,\n" +
                        "  title varchar(15) DEFAULT NULL,\n" +
                        "  author varchar(20) DEFAULT NULL,\n" +
                        "  status varchar(20) DEFAULT NULL,\n" +
                        "  PRIMARY KEY (id)\n" +
                        ");\n" +
                        "\n" +
                        "CREATE TABLE member_detail (\n" +
                        "  id varchar(10) NOT NULL,\n" +
                        "  name varchar(50) DEFAULT NULL,\n" +
                        "  address varchar(50) DEFAULT NULL,\n" +
                        "  contact varchar(12) DEFAULT NULL,\n" +
                        "  PRIMARY KEY (id)\n" +
                        ");\n" +
                        "\n" +
                        "CREATE TABLE borrow_detail (\n" +
                        "  borrowId varchar(10) NOT NULL,\n" +
                        "  date date DEFAULT NULL,\n" +
                        "  memberId varchar(10) DEFAULT NULL,\n" +
                        "  bookId varchar(10) DEFAULT NULL,\n" +
                        "  PRIMARY KEY (borrowId),\n" +
                        "  CONSTRAINT fk_member FOREIGN KEY (memberId) REFERENCES member_detail (id),\n" +
                        "  CONSTRAINT fk_book FOREIGN KEY (bookId) REFERENCES book_detail (id)\n" +
                        ");\n" +
                        "\n" +
                        "CREATE TABLE return_detail (\n" +
                        "  id int NOT NULL,\n" +
                        "  borrowDate date NOT NULL,\n" +
                        "  returnedDate date DEFAULT NULL,\n" +
                        "  fine int DEFAULT NULL,\n" +
                        "  borrowId varchar(10),\n" +
                        "  PRIMARY KEY (id),\n" +
                        "  CONSTRAINT fk_issue FOREIGN KEY (issueid) REFERENCES issuetb (issueId)\n" +
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






