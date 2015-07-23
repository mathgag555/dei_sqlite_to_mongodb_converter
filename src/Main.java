import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;

import java.io.IOException;
import java.net.UnknownHostException;
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

        try{
            MongoDatabase db = connectToMongoDB();

            // Insérer chaque expérimentation dans la BD
            writeExperimentationsToMongo(db, experimentations);

            // Insérer toutes les événements de tâches de chaque expérimentation dans la BD
            experimentations.forEach(experimentation -> writeTasksToMongo(db, experimentation));

            //Commencer avec une seule expe
            //writeTasksToMongo(db, experimentations.get(3));

        }catch (UnknownHostException e){
            e.printStackTrace();
        }
    }

    private static MongoDatabase connectToMongoDB() throws UnknownHostException{
        String host = "10.44.163.154:27017";
        ServerAddress sa = new ServerAddress(host);
        String dbname = "admin";

        MongoClient mc = new MongoClient(sa);
        return mc.getDatabase(dbname);
    }

    private static void writeExperimentationsToMongo(MongoDatabase db, List<Experimentation> experimentations){
        String collectionName = "experimentations";
        MongoCollection<Document> collection = db.getCollection(collectionName);

        experimentations.forEach(experimentation -> {
            Document document = new Document();
            document.put("name", experimentation.getName());
            document.put("duration", experimentation.getDurationInMins());
            document.put("date", experimentation.getDate());
            document.put("time", experimentation.getTime());
            document.put("project", "5auLGd5WdDwvQLXtD");
            document.put("owner", "Y8kZyjRtZjKSi6Qn4");
            collection.insertOne(document);

            experimentation.set_id(document.get("_id").toString());
            System.out.println("expeid:" + experimentation.get_id());
        });
    }

    private static void writeTasksToMongo(MongoDatabase db, Experimentation experimentation) {
        String collectionName = "tasks";
        MongoCollection<Document> collection = db.getCollection(collectionName);

        experimentation.getTasks().forEach(elaboratedTask -> {

            List<Event> events = elaboratedTask.getEvents();
            List<Interval> intervals = elaboratedTask.getSegments();

            JSONArray jsonArrayIntervals = new JSONArray(intervals.toArray());
            JSONArray jsonArrayEvents = new JSONArray(events.toArray());

            DBObject dbObjectIntervals = Utils.encode(jsonArrayIntervals);
            DBObject dbObjectEvents = Utils.encode(jsonArrayEvents);

            System.out.println("Intervals :" + intervals + " pour expe : " + experimentation.getName() + " taskid:" + elaboratedTask.getTaskid() );
            System.out.println("Events :" + events + " pour expe : " + experimentation.getName() + " taskid:" + elaboratedTask.getTaskid() );
            System.out.println("dbObjectIntervals :" + dbObjectIntervals + " pour expe : " + experimentation.getName() + " taskid:" + elaboratedTask.getTaskid() );

            Document document = new Document();
            document.put("taskId", elaboratedTask.getTaskid());
            document.put("expeId", experimentation.get_id());
            document.put("events", dbObjectEvents);
            document.put("segments", dbObjectIntervals);
            document.put("project", "5auLGd5WdDwvQLXtD");
            document.put("owner", "Y8kZyjRtZjKSi6Qn4");
            collection.insertOne(document);
        });
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
                event=new Event(timestamp,state);
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