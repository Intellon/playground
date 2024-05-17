package tutorials.database;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DefaultDBHelper provides the default implementation of the IDBHelper interface,
 * handling various database operations.
 */
public class DefaultDBHelper implements IDBHelper {

    private Connection connection;
    private List<Map<String, Object>> results;
    private Map<String, Map<String, Object>> dbMap;
    private Map<String, List<Map<String, Object>>> dbMapList;

    private static final LoggerManager loggerManager = new LoggerManager(DefaultDBHelper.class);

    /**
     * Default constructor initializing the connection to null.
     */
    public DefaultDBHelper() {
        this.connection = null;
    }

    /**
     * Executes the given SQL query using the provided connection.
     *
     * @param connection the database connection to use for executing the query
     * @param sql the SQL query to be executed
     * @return a list of maps, each representing a row in the result set
     */
    @Override
    public List<Map<String, Object>> executeSQL(Connection connection, String sql) throws RuntimeException {
        this.connection = connection;
        results = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            loggerManager.trace("Execute SQL: " + sql);
            results = writeResultSet(resultSet);
        } catch (SQLException ex) {
            throw new RuntimeException("Exception while executing SQL! " + ex.getMessage());
        }
        return results;
    }

    /**
     * Writes the result set into a list of maps, where each map represents a row.
     *
     * @param resultSet the result set to be processed
     * @return a list of maps representing the result set
     * @throws SQLException if an SQL error occurs
     */
    private List<Map<String, Object>> writeResultSet(ResultSet resultSet) throws SQLException {
        results = new ArrayList<>();
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columns = rsmd.getColumnCount();
        while (resultSet.next()) {
            loggerManager.trace("Table Line: ----------------------------------------");
            LinkedHashMap<String, Object> row = new LinkedHashMap<>(columns);
            for (int i = 1; i <= columns; i++) {
                String colName = rsmd.getColumnName(i);
                row.put(colName, resultSet.getString(colName));
                loggerManager.trace("Column: " + colName + ": " + row.get(colName));
            }
            results.add(row);
        }
        return results;
    }

    /**
     * Finds a line in the result set that contains the specified key-value pair.
     *
     * @param key the column or key of the data
     * @param data the value of the data to be matched
     * @param resultSet the result set to be searched
     * @return a map representing the row containing the specified key-value pair
     */
    @Override
    public Map<String, Object> findLineWithDataInResultSet(String key, Object data, List<Map<String, Object>> resultSet) {
        return resultSet.stream()
                .filter(line -> line.containsKey(key.toUpperCase()) && Objects.equals(line.get(key.toUpperCase()), data))
                .findAny()
                .orElse(Collections.emptyMap());
    }

    /**
     * Normalizes the values in the database result set to strings, converting nulls to "null".
     *
     * @param dbData the database result set to be normalized
     * @return a map with normalized string values
     */
    @Override
    public Map<String, String> normalizeDBData(Map<String, Object> dbData) {
        return dbData.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue())));
    }

    /**
     * Normalize Value in Result Set into String. (null -> given String)
     *
     * @param dbData      DB Result Set to be normalized.
     * @param replaceNull String to replace null values.
     * @return Normalized result set as a map with string values.
     */
    @Override
    public Map<String, String> normalizeDBData(Map<String, Object> dbData, String replaceNull) {
        return dbData.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> replaceNull(e.getValue(), replaceNull)));
    }

    private String replaceNull(Object dbValue, String replacement) {
        return Objects.isNull(dbValue) ? replacement : dbValue.toString();
    }

    /**
     * Format DB Date into the given format.
     *
     * @param date      Date from DB to be formatted.
     * @param srcFormat Source date format in DB.
     * @param tarFormat Target date format to be converted to.
     * @return Formatted date as a string.
     */
    @Override
    public String formatDate(Object date, String srcFormat, String tarFormat) {
        if (Objects.nonNull(srcFormat) && Objects.nonNull(tarFormat) && !srcFormat.isEmpty() && !tarFormat.isEmpty()) {
            return DateTimeUtils.formatSimpleDate(date.toString(), srcFormat, tarFormat);
        } else {
            return date.toString();
        }
    }

    /**
     * Fetch Data in DB Result Set with key into mapped string map, in case key : values in 1 : 1 relationship.
     *
     * @param key    Key for mapping.
     * @param dbData DB result set to be fetched.
     * @return Mapped string map.
     */
    @Override
    public Map<String, Map<String, Object>> fetchDBDataIntoMap(String key, List<Map<String, Object>> dbData) {
        dbMap = new HashMap<>(dbData.size());
        dbData.forEach(row -> {
            Object keyValue = row.get(key);
            if (keyValue != null) {
                dbMap.put(keyValue.toString(), row);
            } else {
                loggerManager.info("Warning: Key value is null for key: " + key);
            }
        });
        return dbMap;
    }

    /**
     * Fetch Data in DB Result Set with keys into mapped string map, in case keys (group) : values in 1 : 1 relationship.
     *
     * @param dbData DB result set to be fetched.
     * @param keys   Key group for mapping.
     * @return Mapped string map.
     */
    @Override
    public Map<String, Map<String, Object>> fetchDBDataIntoMapWithCombKeys(List<Map<String, Object>> dbData, String... keys) {
        dbMap = new HashMap<>(dbData.size());
        dbData.forEach(row -> {
            StringBuilder builder = new StringBuilder();
            for (String key : keys) {
                Object keyValue = row.get(key);
                if (keyValue != null) {
                    builder.append(keyValue);
                } else {
                    loggerManager.warn("Warning: Key value is null for key: " + key);
                }
            }
            dbMap.put(builder.toString(), row);
        });
        return dbMap;
    }

    /**
     * Fetch Data in DB Result Set with key into mapped string map, in case key : values in 1 : n relationship.
     *
     * @param key    Key for mapping.
     * @param dbData DB result set to be fetched.
     * @return Mapped string map with list values.
     */
    @Override
    public Map<String, List<Map<String, Object>>> fetchDBDataIntoMapListWithKey(String key, List<Map<String, Object>> dbData) {
        dbMapList = new HashMap<>(dbData.size());
        dbData.forEach(row -> {
            String uniqueKey = String.valueOf(row.get(key));
            dbMapList.computeIfAbsent(uniqueKey, k -> new LinkedList<>()).add(row);
        });
        return dbMapList;
    }

    /**
     * Convert DB Keys to lowercase or uppercase.
     *
     * @param dataMap      DB Result Set to be converted.
     * @param isToLowercase Flag to convert keys to lowercase.
     * @return Converted map with keys in lowercase or uppercase.
     */
    @Override
    public Map<String, Object> convertKeyTo(Map<String, Object> dataMap, boolean isToLowercase) {
        if (Objects.nonNull(dataMap) && !dataMap.isEmpty()) {
            Map<String, Object> theMap = new HashMap<>(dataMap.size());
            dataMap.forEach((key, value) -> theMap.put(isToLowercase ? key.toLowerCase() : key.toUpperCase(), value));
            return theMap;
        } else {
            return dataMap;
        }
    }
}
