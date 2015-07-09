import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ranj2004 on 15-06-30.
 */
public class Utils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH'h'mm.ss");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH'h'mm.ss");


    public static LocalDateTime parseTimeStamp(String timestamp) {
        return LocalDateTime.parse(timestamp,formatter);
    }

    public static String parseDate(LocalDateTime date) {
        return date.format(dateFormatter);
    }

    public static String parseTime(LocalDateTime time) {
        return time.format(timeFormatter);
    }

    public static Duration getLocalDuration(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end);
    }

    public static String parseName(String name){
        String foundName = null;

        System.out.println(name);

        Pattern pattern = Pattern.compile("^.*(EXP\\w+)$");
        Matcher matcher = pattern.matcher(name);
        matcher.matches();
        System.out.println(matcher.start()+":"+matcher.end());
        return matcher.group(1);
    }
}
