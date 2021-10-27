package tech.goodquestion.lembot.database;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnector {

    private static MysqlConnectionPoolDataSource dataSource;

    private static void setupDataSource() {
        DatabaseConfig config = Config.getInstance().getDatabase();
        dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setURL(config.getUri());
        dataSource.setUser(config.getUsername());
        dataSource.setPassword(config.getPassword());
    }

    public static Connection openConnection() {
        try {
            if (dataSource == null) {
                setupDataSource();
            }

            return dataSource.getConnection();
        } catch (SQLException sqlException) {
            throw new RuntimeException("database connection couldn't be established");
        }
    }

}
