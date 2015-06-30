import java.io.File;
import java.lang.System;
import java.sql.*;

public class main {

	public static void main(String[] args) {
        Connection c = null;
        String defaultdbName = "db_files"+ File.separator +"2015-01-16_14-32_EXP23.db";

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + defaultdbName);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            parseEnregistrements(c);

            c.close();
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

	private static void parseEnregistrements(Connection c){


        try {
			/*
			 * BufferedReader br = new BufferedReader(new InputStreamReader(
			 * System.in));
			 * System.out.println("Entrez le nom complet du fichier"); String
			 * dbName = br.readLine();
			 */

            Statement stmt = null;
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
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }
}