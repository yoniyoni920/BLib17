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
			connection = DriverManager.getConnection("jdbc:mysql://localhost/blib?serverTimezone=Asia/Jerusalem", "root", "Aa123456");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Statement createStatement() throws SQLException {
		return getConnection().createStatement();
    }

	public static PreparedStatement prepareStatement(String sql) throws SQLException {
		return getConnection().prepareStatement(sql);
	}
}
