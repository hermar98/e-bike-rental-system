package main.java.com.team15.ebrs.util;


import java.sql.Connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
/**
 * Class used to create a connection pool used in the Database Access Layer.
 * @author Team 15.
 */

public class DatabaseConnector {
	
	private static String dbDriver = "com.mysql.jdbc.Driver";
	private static String dbUrl = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/hermanrm?autoReconnect=true&useSSL=false";
	private static String username = "hermanrm";
	private static String password = "IgQoC7fM";
	
	private static String testDbUrl = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/trondjro?autoReconnect=true&useSSL=false";
	private static String testUsername = "trondjro";
	private static String testPassword = "6PfS2Yvz";
	
	private static String sqlScriptPath = "/main/resources/com/team15/ebrs/CreateTables.sql";
	private static String testSqlScriptPath = "/main/resources/com/team15/ebrs/CreateTablesTest.sql";
	
	private static boolean testDbSelected = false;
	
	private static ComboPooledDataSource cpds;

	/**
	 * Initializes the connection pool. Checks whether it should connect to test database.
	 * @throws Exception
	 */
	private static void initialize() throws Exception {
		if (!testDbSelected) {
			cpds = new ComboPooledDataSource();
			cpds.setDriverClass(dbDriver);
			cpds.setJdbcUrl(dbUrl);
			cpds.setUser(username);
			cpds.setPassword(password);
			cpds.setAcquireRetryAttempts(2);
		} else {
			cpds = new ComboPooledDataSource();
			cpds.setDriverClass(dbDriver);
			cpds.setJdbcUrl(testDbUrl);
			cpds.setUser(testUsername);
			cpds.setPassword(testPassword);
			cpds.setAcquireRetryAttempts(2);
		}
	}

	/**
	 * Fetches a connection from the pool.
	 * @return Connection from the specified connection pool.
	 * @throws Exception
	 */
	public static synchronized Connection getConnection() throws Exception {
        if (cpds == null) {
            initialize();
        }
        return cpds.getConnection();
    }

	/**
	 * Method used for JUnit tests of the DAO classes. Enables the connection pool to use the test database.
	 * @param b Boolean stating whether you want to use the test database. 
	 * @throws Exception
	 */
	public static void useTestDatabase(boolean b) throws Exception {
		if (b != testDbSelected) {
			testDbSelected = b;
			initialize();
		}
	}

	/**
	 * Runs a sqlScript that resets the current database.
	 * @throws Exception
	 */
	public static void resetDatabase() throws Exception {
		if (cpds == null) {
            initialize();
        }
		if (!testDbSelected) {
			RunSqlScript.run(sqlScriptPath);
		} else {
			RunSqlScript.run(testSqlScriptPath);
		}
	}
	

}
