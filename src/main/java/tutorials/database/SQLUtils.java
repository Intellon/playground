package tutorials.database;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * SQLUtils provides utility methods for SQL operations, such as reading SQL statements from files.
 */
public class SQLUtils {

    private static final LoggerManager loggerManager = new LoggerManager(SQLUtils.class);

    /**
     * Retrieves an SQL statement from the given content or file name.
     *
     * If the provided string ends with ".sql", it treats the string as a file name and reads the SQL statement from the file.
     * Otherwise, it assumes the string is an SQL statement itself, provided it starts with "select".
     *
     * @param sqlContentOrFilename the SQL content or the file name containing the SQL statement
     * @return the SQL statement as a string
     * @throws Exception if the file cannot be found, is empty, or the content is not a valid SQL statement
     */
    public String getSQLStatement(String sqlContentOrFilename) {
        String sqlStatement = sqlContentOrFilename;
        if (sqlContentOrFilename.endsWith(".sql")) {
            try {
                sqlStatement = new String(Files.readAllBytes(Paths.get(sqlContentOrFilename)));
                if (sqlStatement.isEmpty()) {
                    loggerManager.error("Given .sql file is empty or cannot be read! File: " + sqlContentOrFilename);
                }
            } catch (IOException e) {
                loggerManager.error("Error reading the SQL file: " + sqlContentOrFilename, e);
            }
        } else if (!sqlContentOrFilename.trim().toLowerCase().startsWith("select")) {
            loggerManager.error("Given content is neither a .sql file nor a SELECT statement");
        }
        return sqlStatement;
    }
}


