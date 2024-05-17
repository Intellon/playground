package tutorials.database;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * DBConnector is the main class for managing database connections and executing SQL queries.
 * It uses IDBHelper and IDBConnectionManager interfaces for abstraction and separation of concerns.
 */
public class DBConnector {

    private Connection connection;
    private final IDBHelper dbHelper;
    private final IDBConnectionManager connectionManager;
    private final SQLUtils sqlUtils;

    private String dbType;
    private String host;
    private String user;
    private String port;
    private String dbName;
    private String password;

    private static final LoggerManager loggerManager = new LoggerManager(DBConnector.class);

    public DBConnector() {
        this.dbHelper = new DefaultDBHelper();
        this.connectionManager = new DefaultDBConnectionManager();
        this.sqlUtils = new SQLUtils();
    }

    // Konstruktor mit Dependency Injection
    public DBConnector(IDBHelper dbHelper, IDBConnectionManager connectionManager, SQLUtils sqlUtils) {
        this.dbHelper = new DefaultDBHelper();
        this.connectionManager = new DefaultDBConnectionManager();
        this.sqlUtils = new SQLUtils();
    }

    /**
     * Sets the database configuration parameters.
     *
     * @param dbType   the type of the database (e.g., "mysql", "oracle-SID", "oracle-SN", "mssql")
     * @param host     the host of the database server
     * @param user     the username for the database connection
     * @param port     the port number of the database server
     * @param dbName   the name of the database or service name or service ID
     * @param password the password for the database connection
     */
    public void setDBConfig(String dbType, String host, String user, String port, String dbName, String password) {
        this.dbType = dbType;
        this.host = host;
        this.user = user;
        this.port = port;
        this.dbName = dbName;
        this.password = password;
    }

    /**
     * Establishes a connection to the database using the configured parameters.
     */
    public void connect() {
        this.connection = connectionManager.createConnection(dbType, host, user, port, dbName, password);
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        connectionManager.closeConnection(this.connection);
    }

    /**
     * Executes the given SQL query.
     *
     * @param sql the SQL query to be executed
     * @return a list of maps, each representing a row in the result set
     * @throws Error if the connection is not established
     */
    public List<Map<String, Object>> execute(String sql) {
        if (this.connection == null) {
            loggerManager.error("Connection is not established.");
        }
        return dbHelper.executeSQL(this.connection, sql);
    }

    /**
     * Retrieves an SQL statement from the given content or file name.
     *
     * @param sqlContentOrFilename the SQL content or the file name containing the SQL statement
     * @return the SQL statement as a string
     */
    public String getSQLStatement(String sqlContentOrFilename) {
        return sqlUtils.getSQLStatement(sqlContentOrFilename);
    }


    /**
     * Finds a line in the result set that contains the specified key-value pair.
     *
     * @param key       the column or key of the data
     * @param data      the value of the data to be matched
     * @param resultSet the result set to be searched
     * @return a map representing the row containing the specified key-value pair
     */
    public Map<String, Object> findLineWithDataInResultSet(String key, Object data, List<Map<String, Object>> resultSet) {
        return dbHelper.findLineWithDataInResultSet(key, data, resultSet);
    }

    /**
     * Normalizes the values in the database result set to strings, converting nulls to "null".
     *
     * @param dbData the database result set to be normalized
     * @return a map with normalized string values
     */
    public Map<String, String> normalizeDBData(Map<String, Object> dbData) {
        return dbHelper.normalizeDBData(dbData);
    }

    /**
     * Normalizes the values in the database result set to strings, converting nulls to the specified replacement string.
     *
     * @param dbData      the database result set to be normalized
     * @param replaceNull the string to replace null values
     * @return a map with normalized string values
     */
    public Map<String, String> normalizeDBData(Map<String, Object> dbData, String replaceNull) {
        return dbHelper.normalizeDBData(dbData, replaceNull);
    }

    /**
     * Formats the given database date into the target format.
     *
     * @param date      the date from the database to be formatted
     * @param srcFormat the source date format in the database
     * @param tarFormat the target date format to be converted to
     * @return the formatted date as a string
     */
    public String formatDate(Object date, String srcFormat, String tarFormat) {
        return dbHelper.formatDate(date, srcFormat, tarFormat);
    }

    /**
     * Fetches data in the database result set with the specified key into a mapped string map,
     * assuming a 1:1 relationship between keys and values.
     *
     * @param key    the key for mapping
     * @param dbData the database result set to be fetched
     * @return a mapped string map
     */
    public Map<String, Map<String, Object>> fetchDBDataIntoMap(String key, List<Map<String, Object>> dbData) {
        return dbHelper.fetchDBDataIntoMap(key, dbData);
    }

    /**
     * Fetches data in the database result set with the specified keys into a mapped string map,
     * assuming a 1:1 relationship between key groups and values.
     *
     * @param dbData the database result set to be fetched
     * @param keys   the key group for mapping
     * @return a mapped string map
     */
    public Map<String, Map<String, Object>> fetchDBDataIntoMapWithCombKeys(List<Map<String, Object>> dbData, String... keys) {
        return dbHelper.fetchDBDataIntoMapWithCombKeys(dbData, keys);
    }

    /**
     * Fetches data in the database result set with the specified key into a mapped string map,
     * assuming a 1:n relationship between keys and values.
     *
     * @param key    the key for mapping
     * @param dbData the database result set to be fetched
     * @return a mapped string map with list values
     */
    public Map<String, List<Map<String, Object>>> fetchDBDataIntoMapListWithKey(String key, List<Map<String, Object>> dbData) {
        return dbHelper.fetchDBDataIntoMapListWithKey(key, dbData);
    }

    /**
     * Converts the keys in the database result set to lowercase or uppercase.
     *
     * @param dataMap      the database result set to be converted
     * @param isToLowercase flag to convert keys to lowercase
     * @return a converted map with keys in lowercase or uppercase
     */
    public Map<String, Object> convertKeyTo(Map<String, Object> dataMap, boolean isToLowercase) {
        return dbHelper.convertKeyTo(dataMap, isToLowercase);
    }

    /**
     * Executes the sequence of connecting to the database, executing the SQL query, and closing the connection using provided parameters.
     *
     * @param dbType   the type of the database (e.g., "mysql", "oracle-SID", "oracle-SN", "mssql")
     * @param host     the host of the database server
     * @param user     the username for the database connection
     * @param port     the port number of the database server
     * @param dbName   the name of the database or service name or service ID
     * @param password the password for the database connection
     * @param sql      the SQL query to be executed
     * @return a list of maps, each representing a row in the result set
     */
    public List<Map<String, Object>> connectExecuteClose(String dbType, String host, String user, String port, String dbName, String password, String sql) {
        setDBConfig(dbType, host, user, port, dbName, password);
        connect();
        try {
            return execute(sql);
        } finally {
            close();
        }
    }
}
