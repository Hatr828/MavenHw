package org.brainacad.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public final class Database {
    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    private Database(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public static Database fromEnv() {
        Map<String, String> env = System.getenv();
        String host = env.getOrDefault("PGHOST", "localhost");
        int port = parsePort(env.getOrDefault("PGPORT", "5432"));
        String database = env.getOrDefault("PGDATABASE", "kavyarnya");
        String user = env.getOrDefault("PGUSER", "postgres");
        String password = env.getOrDefault("PGPASSWORD", "postgres");
        return new Database(host, port, database, user, password);
    }

    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl(), user, password);
    }

    public String jdbcUrl() {
        return String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
    }

    private static int parsePort(String port) {
        try {
            return Integer.parseInt(port);
        } catch (NumberFormatException ex) {
            return 5432;
        }
    }
}
