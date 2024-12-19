package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBControl {
	private static DBControl instance;
	private Connection connection;
	
	public static DBControl getInstance() {
		if (instance == null) {
			instance = new DBControl();
		}
		return instance;
	}

	public static Connection getConnection() {
		return getInstance().connection;
	}
	
	private DBControl() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/blib?serverTimezone=IST", "root", "Aa123456");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
