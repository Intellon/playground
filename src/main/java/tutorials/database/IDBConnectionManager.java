package tutorials.database;

import java.sql.Connection;

/**
 * IDBConnectionManager defines the methods for managing database connections.
 */
public interface IDBConnectionManager {
    Connection createConnection(String dbType, String host, String user, String port, String dbName, String password);
    void closeConnection(Connection connection);
}
