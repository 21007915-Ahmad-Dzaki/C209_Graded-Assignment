package project_3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

	private  static String dbURL;
	private  static String dbUsername;
	private  static String dbPassword;
	
	private static Connection conn;
	private static Statement statement;
	private static ResultSet resultSet;

	public static void init(String url, String user, String pw)
	{
		dbURL = url;
		dbUsername = user;
		dbPassword = pw;
	}

	private static void connect() {
		try {

			conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);

		} catch (SQLException se) {
			System.out.println("SQL Connection Error: " + se.getMessage());
		} 
	}

		public static ResultSet getTable(String sqlStr) {
		try {
			connect();

			statement = conn.createStatement();
			resultSet = statement.executeQuery(sqlStr);

		} catch (SQLException se) {
			System.out.println("SQL Error: " + se.getMessage());
		} 

		return resultSet;
	}

	public static int execSQL(String sqlStr) {
		int affectedRows = 0;
		try {
			connect();
			statement = conn.createStatement();
			affectedRows = statement.executeUpdate(sqlStr);

		} catch (SQLException se) {
			System.out.println("SQL Error: " + se.getMessage());
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		return affectedRows;
	}

	public static void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {

		}
	}
}
