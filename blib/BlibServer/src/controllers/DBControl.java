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
	 * @return
	 */
	public static DBControl getInstance() {
		if (instance == null) {
			instance = new DBControl();
		}
		return instance;
	}

	/**
	 * Returns intance of connection
	 * @return
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
	 * Creates the statements
	 * @return
	 * @throws SQLException
	 */
	public static Statement createStatement() throws SQLException {
		return getConnection().createStatement();
    }

	/**
	 * Prepares the statements
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static PreparedStatement prepareStatement(String sql) throws SQLException {
		return getConnection().prepareStatement(sql);
	}
}
