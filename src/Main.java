import java.io.IOException;
import java.lang.System;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args) {
        String dbName = null;
        List<Experimentation> experimentations = null;

        try{

            experimentations = Files.walk(Paths.get("db_files"))
                    .filter(path -> !path.toFile().isDirectory())
                    .map(Main::parseExperimentation)
                    .collect(Collectors.toList());

            experimentations.forEach(System.out::println);

        } catch (NullPointerException | IOException e){
            e.printStackTrace();
        }
    }

    private static Experimentation parseExperimentation(Path dbPath){
        Connection c = null;
        Path dbName = dbPath;
        Experimentation experimentation = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            Scenario scenario = parseScenarios(c);
            Collection<RawTask> rawTasks = parseEnregistrements(c, scenario).values();
            List<ElaboratedTask> elaboratedTasks = rawTasks.stream()
                    .map(rawTask -> rawTask.elaborate(scenario.getStartTime(), scenario.getEndTime()))
                    .collect(Collectors.toList());

/*            rawTasks.forEach(rawTask -> {
                System.out.println(rawTask);
                System.out.println(rawTask.elaborate(scenario.getStartTime(), scenario.getEndTime()));
            });*/

            experimentation = new Experimentation(scenario, rawTasks, elaboratedTasks);

            //System.out.println(experimentation);

            c.close();
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            System.err.println(dbName);
        }

        System.out.println("ParseExperimentation operation done successfully");
        return experimentation;
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

            //System.out.println(scenario);

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

            Statement stmt = null;
            stmt = c.createStatement();
            String query =
                    "SELECT * FROM recording_task WHERE timestamp >= '" +
                    scenario.getStartTime_s() +
                    "' AND timestamp <= '" +
                    scenario.getEndTime_s() + "';";

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
                task.addEvent(event);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
//        tasks.values().forEach(task->System.out.println(task.toString()));
        System.out.println("ParseEnregistrement operation done successfully");
        return tasks;
    }
}