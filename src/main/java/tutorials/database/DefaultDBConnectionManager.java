package tutorials.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DefaultDBConnectionManager provides the default implementation of the IDBConnectionManager interface,
 * handling the creation and closing of database connections.
 */
public class DefaultDBConnectionManager implements IDBConnectionManager {

    private static final LoggerManager loggerManager = new LoggerManager(DefaultDBConnectionManager.class);

    /**
     * Creates a connection to the specified database using the provided parameters.
     *
     * @param dbType   the type of the database (e.g., "mysql", "oracle-SID", "oracle-SN", "mssql")
     * @param host     the host of the database server
     * @param user     the username for the database connection
     * @param port     the port number of the database server
     * @param dbName   the name of the database or service name or service ID
     * @param password the password for the database connection
     * @return a Connection object representing the database connection
     * @throws Exception if there is an error connecting to the database or loading the database driver
     */
    @Override
    public Connection createConnection(String dbType, String host, String user, String port, String dbName, String password) {
        String url = "";
        try {
            switch (dbType) {
                case "mysql" -> {
                    Class.forName("com.mysql.jdbc.Driver");
                    url = "jdbc:mysql://" + host;
                }
                case "oracle-SID" -> {
                    Class.forName("oracle.jdbc.OracleDriver");
                    url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
                }
                case "oracle-SN" -> {
                    Class.forName("oracle.jdbc.OracleDriver");
                    url = "jdbc:oracle:thin:@" + host + ":" + port + "/" + dbName;
                }
                case "mssql" -> {
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    url = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;";
                }
                case "h2" -> {
                    Class.forName("org.h2.Driver");
                    url = "jdbc:h2:mem:" + dbName + ";DB_CLOSE_DELAY=-1";
                }
                default -> loggerManager.error("db is not set well: " + dbType);
            }
            loggerManager.debug("Try to connect to DB: " + url);
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException ex) {
            loggerManager.error("Exception while connecting to DB! " + ex.getMessage());
        }
        return null;
    }

    /**
     * Closes the given database connection.
     *
     * @param connection the database connection to be closed
     * @throws Exception if there is an error closing the connection
     */
    @Override
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                loggerManager.error("Exception while closing connection! " + ex.getMessage());
            }
        }
    }
}
