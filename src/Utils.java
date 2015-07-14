import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;


/**
 * Created by ranj2004 on 15-06-30.
 */
public class Utils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH'h'mm.ss");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH'h'mm.ss");


    public static LocalDateTime parseTimeStamp(String timestamp) {
        return LocalDateTime.parse(timestamp, formatter);
    }

    public static String parseDate(LocalDateTime date) {
        return date.format(dateFormatter);
    }

    public static String parseTime(LocalDateTime time) {
        return time.format(timeFormatter);
    }

    public static double getLocalDuration(LocalDateTime start, LocalDateTime end) {
        return toRawMinutes(Duration.between(start, end));
    }

    public static double toRawMinutes(Duration duration){
        long deltaSeconds = duration.getSeconds() - (duration.toMinutes() * 60);
        return duration.toMinutes() + deltaSeconds / 60.0;
    }

    public static DBObject encode(JSONArray a) {
        BasicDBList result = new BasicDBList();
        try {
            for (int i = 0; i < a.length(); ++i) {
                Object o = a.get(i);
                if (o instanceof JSONObject) {
                    result.add(encode((JSONObject)o));
                } else if (o instanceof JSONArray) {
                    result.add(encode((JSONArray)o));
                } else {
                    result.add(o);
                }
            }
            return result;
        } catch (JSONException je) {
            return null;
        }
    }

    public static DBObject encode(JSONObject o) {
        BasicDBObject result = new BasicDBObject();
        try {
            Iterator i = o.keys();
            while (i.hasNext()) {
                String k = (String)i.next();
                Object v = o.get(k);
                if (v instanceof JSONArray) {
                    result.put(k, encode((JSONArray)v));
                } else if (v instanceof JSONObject) {
                    result.put(k, encode((JSONObject)v));
                } else {
                    result.put(k, v);
                }
            }
            return result;
        } catch (JSONException je) {
            return null;
        }
    }

}
