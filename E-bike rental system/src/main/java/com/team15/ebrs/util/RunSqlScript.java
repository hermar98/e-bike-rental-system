package main.java.com.team15.ebrs.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;

import com.ibatis.common.jdbc.ScriptRunner;

/**
 * Class used to run a sql script.
 * @author Team 15.
 */
public class RunSqlScript {
	/**
	 * Runs a sqlScript using the provided file path.
	 * @param sqlFilePath The path for the sql file you want to run.
	 */
	public static void run(String sqlFilePath) {
		try {
			// Initialize object for ScripRunner
			Connection con = DatabaseConnector.getConnection();
			ScriptRunner sr = new ScriptRunner(con, false, false); // Missing ScriptRunner?

			// Give the input file to Reader
			Reader reader = new BufferedReader(
                               new InputStreamReader(RunSqlScript.class.getResourceAsStream(sqlFilePath)));

			// Execute script
			sr.runScript(reader);

		} catch (Exception e) {
			System.err.println("Failed to Execute" + sqlFilePath
					+ " The error is " + e.getMessage());
		}
	}
}
