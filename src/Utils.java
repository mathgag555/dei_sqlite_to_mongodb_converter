import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Created by ranj2004 on 15-06-30.
 */
public class Utils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH'h'mm.ss");

    public static LocalDateTime parseTimeStamp(String timestamp) {
        return LocalDateTime.parse(timestamp,formatter);
    }

    public static Duration getLocalDuration(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start,end);
    }
}
