package com.example.transporte.conexion;

import java.sql.*;

public class Conexion {
    private static Connection connection;
    private static String db_url = "jdbc:mysql://127.0.0.1:3306/transporte?charcaterEncoding=utf8";
    private static String user = "root";
    private static String password = "1234";

    // Abre la conexion
    public static Connection initConnection() {
        try {
            connection = DriverManager.getConnection(db_url, user, password);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    //
    public static Connection getConnection() {
        if (connection != null){
            System.out.println("conectado");
            return  connection;
        }
        return null;
    }
    // Cierra la conexion
    public static boolean closeConnection() {
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
