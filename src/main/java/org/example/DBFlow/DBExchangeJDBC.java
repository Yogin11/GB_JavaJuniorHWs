package org.example.DBFlow;

import org.example.model.Course;
import org.example.model.RepositoryCourses;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DBExchangeJDBC {

    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static String dbName = "SchoolDB";

    public static void dbCreator() {
        getProperties();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            createDB(connection);
            createTable(connection);
            fillDBWithDATA(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createDB(Connection connection) throws SQLException {
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS ";
        String useDB = "USE ";
        try (PreparedStatement statement = connection.prepareStatement(createDatabaseSQL + dbName)) {
            statement.execute(useDB + dbName);
        }
    }
    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + Course.tableName +
                                " (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NULL, duration VARCHAR(10) NULL);";

        try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
            statement.execute();
        }
    }

    private static void fillDBWithDATA(Connection connection) throws SQLException {
        Course course;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("('");
        for (int i = 0; i < 11; i++) {
            course = RepositoryCourses.coursesCreator();
            stringBuffer.append(course.getName())
                    .append("','")
                    .append(course.getDuration());
            stringBuffer.append(i==10?"');":"'),('");
        }
        String insertData = "INSERT INTO " + Course.tableName + " (name, duration) VALUES " + stringBuffer.toString();
        try (PreparedStatement statement = connection.prepareStatement(insertData)) {
            statement.execute();
        }

    }

    public static void getProperties() {
        try {
            Properties props = new Properties();
            try (InputStream in = Files.newInputStream(Path.of("dbsource/properties"))) {
                props.load(in);
            }
            URL = props.getProperty("url");
            USER = props.getProperty("user");
            PASSWORD = props.getProperty("password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
