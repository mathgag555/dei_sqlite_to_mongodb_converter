import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class main {

	public static void main(String[] args) {
		Connection c = null;
		Statement stmt = null;

		try {
			/*
			 * BufferedReader br = new BufferedReader(new InputStreamReader(
			 * System.in));
			 * System.out.println("Entrez le nom complet du fichier"); String
			 * dbName = br.readLine();
			 */
			String defaultdbName = "2015-06-26_17-46_EXPMCI23.db";

			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite: " + defaultdbName);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM recording_task;");
			while (rs.next()) {
				int id = rs.getInt("id");
				String scenarioName = rs.getString("scenario_name");
				int taskId = rs.getInt("task_id");
				String state = rs.getString("state");
				String timestamp = rs.getString("timestamp");

				System.out.println("ID = " + id);
				System.out.println("scenario name = " + scenarioName);
				System.out.println("taskId = " + taskId);
				System.out.println("state = " + state);
				System.out.println("timestamp = " + timestamp);

			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Operation done successfully");
	}
}