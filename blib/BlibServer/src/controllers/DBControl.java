package controllers;


import java.sql.*;

/**
 *  The class handles first communication  between the server and DB
 *  Prevents the connection from being opened a few times.
 *  Prevents DRY.
 */
public class DBControl {
	private static DBControl instance;
	private Connection connection;

	/**
	 * Singleton for multiple connection prevention.
	 * @return The current instance of the DBControl
	 */
	public static DBControl getInstance() {
		if (instance == null) {
			instance = new DBControl();
		}
		return instance;
	}

	/**
	 * @return connection of the current instance
	 */
	public static Connection getConnection() {
		return getInstance().connection;
	}

	/**
	 * Opens communication to the DB
	 */
	private DBControl() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/blib?serverTimezone=Asia/Jerusalem", "root", "Aa123456");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates an SQL statement
	 * @return A new statement
	 * @throws SQLException
	 */
	public static Statement createStatement() throws SQLException {
		return getConnection().createStatement();
    }

	/**
	 * Prepares the statements
	 * @param sql The query to execute in the prepared statement
	 * @return A new prepared statement
	 * @throws SQLException
	 */
	public static PreparedStatement prepareStatement(String sql) throws SQLException {
		return getConnection().prepareStatement(sql);
	}
}
