import java.io.File;
import java.lang.System;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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

	private static HashMap<Integer,RawTask> parseEnregistrements(Connection c){
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM recording_task;");
            while (rs.next()) {
                RawTask task,recoveredTask ;
                Event event;
                int id = rs.getInt("id");
                String scenarioName = rs.getString("scenario_name");
                int taskId = rs.getInt("task_id");
                String state = rs.getString("state");
                String timestamp = rs.getString("timestamp");
                task=new RawTask(scenarioName,taskId);
                event=new Event(timestamp,Event.State.valueOf(state));
                if((recoveredTask=tasks.putIfAbsent(taskId,task))!=null){
                    task=recoveredTask;
                }
                task.getEvents().add(event);
//
//
//                System.out.println("ID = " + id);
//                System.out.println("scenario name = " + scenarioName);
//                System.out.println("taskId = " + taskId);
//                System.out.println("state = " + state);
//                System.out.println("timestamp = " + timestamp);

            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        tasks.values().forEach(task->System.out.println(task.toString()));
        System.out.println("Operation done successfully");
        return tasks;
    }
}