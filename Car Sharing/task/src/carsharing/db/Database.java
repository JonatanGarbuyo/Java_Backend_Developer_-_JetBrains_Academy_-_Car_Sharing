package carsharing.db;

import org.h2.jdbcx.JdbcDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private static final String CONNECTION_URL = "jdbc:h2:./src/carsharing/db/";
    private final String databaseFileName;
    private Connection connection = null;

    public Database(String databaseFileName) {
        this.databaseFileName = databaseFileName;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null) {
            JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL(CONNECTION_URL + databaseFileName);
            this.connection = dataSource.getConnection();
        }
        return this.connection;
    }
}
