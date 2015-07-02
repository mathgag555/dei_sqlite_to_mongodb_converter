import java.io.File;
import java.lang.System;
import java.sql.*;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
        Connection c = null;
        String defaultdbName = "db_files"+ File.separator +"2015-06-26_17-46_EXPMCI23.db";

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + defaultdbName);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            Scenario scenario = parseScenarios(c);
            parseEnregistrements(c, scenario);

            c.close();
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private static Scenario parseScenarios(Connection c) {
        Scenario scenario = null;

        try {
            Statement stmt = null;

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM scenario ORDER BY start_time DESC;");

            rs.next();
            String name = rs.getString("name");
            String startTime = rs.getString("start_time");
            String endTime = rs.getString("end_time");

            scenario = new Scenario(name, startTime, endTime);

            System.out.println(scenario);

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("ParseScenario operation done successfully");
        return scenario;
    }

    private static HashMap<Integer,RawTask> parseEnregistrements(Connection c, Scenario scenario){
        HashMap<Integer,RawTask> tasks = new HashMap<>();
        try {
			/*
			 * BufferedReader br = new BufferedReader(new InputStreamReader(
			 * System.in));
			 * System.out.println("Entrez le nom complet du fichier"); String
			 * dbName = br.readLine();
			 */

            Statement stmt = null;
            stmt = c.createStatement();
            String query =
                    "SELECT * FROM recording_task WHERE timestamp >= '" +
                    scenario.getStartTime_s() +
                    "' AND timestamp <= '" +
                    scenario.getEndTime_s() + "';";

            System.out.println(query);

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                RawTask task,recoveredTask ;
                Event event;
                int taskId = rs.getInt("task_id");
                String state = rs.getString("state");
                String timestamp = rs.getString("timestamp");
                task=new RawTask(taskId);
                event=new Event(timestamp,Event.State.valueOf(state));
                if((recoveredTask=tasks.putIfAbsent(taskId,task))!=null){
                    task=recoveredTask;
                }
                task.getEvents().add(event);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        tasks.values().forEach(task->System.out.println(task.toString()));
        System.out.println("ParseEnregistrement operation done successfully");
        return tasks;
    }
}