package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
			connection = DriverManager.getConnection("jdbc:mysql://localhost/blib?serverTimezone=IST", "root", "EliasFarah3!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
