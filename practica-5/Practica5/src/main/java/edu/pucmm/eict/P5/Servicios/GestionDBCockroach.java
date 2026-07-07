package edu.pucmm.eict.P5.Servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestionDBCockroach {
    private static final String URL = System.getenv("JDBC_DATABASE_URL");


    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
