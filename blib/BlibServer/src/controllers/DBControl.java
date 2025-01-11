package controllers;

import services.MapUtils;

import java.sql.*;
import java.util.Map;

/*
 * The class handles first communication  between the server and DB
 * Prevents the connection from being opened a few times.
 * Prevents DRY.
 */
public class DBControl {
	private static DBControl instance;
	private Connection connection;
	/*
	 * Singleton for multiple connection prevention.
	 */
	public static DBControl getInstance() {
		if (instance == null) {
			instance = new DBControl();
		}
		return instance;
	}

	public static Connection getConnection() {
		return getInstance().connection;
	}
	/*
	 * Opens communication to the DB
	 */
	private DBControl() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/blib?serverTimezone=IST", "root", "Aa123456");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Statement createStatement() {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

	public PreparedStatement prepareStatement(String sql) {
		try {
			return connection.prepareStatement(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method is a utility method that can build you a select query easily.
	 * The method assumes you need all columns and uses AND for separating where arguments
	 * <br><br>
	 * It works by first receiving the table name followed by key-value pairs
	 * of WHERE arguments which are then turned into a map of string -> object
	 * <br>
	 * It returns a prepared statement object which you will need to close
	 * Examples:
	 * <br><br>
	 * SELECT * FROM users WHERE id = 1
	 * <br>
	 * selectQuery("users", "id", "1")
	 * <br>
	 * SELECT * FROM users WHERE id = 1 AND something = 'else'
	 * br>
	 * selectQuery("users", "id", 1, "something", "else")
	 *
	 * @param tableName
	 * @param whereArgs
	 * @return PreparedStatement
	 */
	public PreparedStatement selectQuery(String tableName, Object ...whereArgs) {
		Map<String, Object> whereMap = MapUtils.mapOf(whereArgs);

        try {
			StringBuilder sb = new StringBuilder();

			sb.append("SELECT * FROM ");
			sb.append(tableName);

			if (!whereMap.isEmpty()) {
				sb.append(" WHERE ");
			}

			boolean putAnd = false;
			for (String key : whereMap.keySet()) {
				if (putAnd) {
					sb.append("AND ");
				}
				sb.append(key);
				sb.append(" = ? ");
				putAnd = true;
			}

			PreparedStatement stt = prepareStatement(sb.toString());
			int i = 1;
			for (Object value : whereMap.values()) {
				stt.setObject(i++, value);
			}
			System.out.println("Query: " + sb.toString());

			return stt;
		} catch (SQLException e) {
            throw new RuntimeException(e);
        }

	}
	

}
