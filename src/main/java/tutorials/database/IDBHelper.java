package tutorials.database;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * IDBHelper defines the methods for database operations such as executing SQL queries,
 * finding data in result sets, normalizing data, formatting dates, and converting keys.
 */
public interface IDBHelper {
    List<Map<String, Object>> executeSQL(Connection connection, String sql);
    Map<String, Object> findLineWithDataInResultSet(String key, Object data, List<Map<String, Object>> resultSet);
    Map<String, String> normalizeDBData(Map<String, Object> dbData);
    Map<String, String> normalizeDBData(Map<String, Object> dbData, String replaceNull);
    String formatDate(Object date, String srcFormat, String tarFormat);
    Map<String, Map<String, Object>> fetchDBDataIntoMap(String key, List<Map<String, Object>> dbData);
    Map<String, Map<String, Object>> fetchDBDataIntoMapWithCombKeys(List<Map<String, Object>> dbData, String... keys);
    Map<String, List<Map<String, Object>>> fetchDBDataIntoMapListWithKey(String key, List<Map<String, Object>> dbData);
    Map<String, Object> convertKeyTo(Map<String, Object> dataMap, boolean isToLowercase);
}