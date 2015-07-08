import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Created by ranj2004 on 15-06-30.
 */
@ToString
public class Event implements Comparable<Event> {
    public Event(String timestamp, State state) {
        this.timestamp = Utils.parseTimeStamp(timestamp);
        this.state = state;
    }

    @Override
    public int compareTo(Event o) {
        return this.timestamp.toString().compareTo(o.getTimestamp().toString());
    }

    enum State {
        ACTIVE,
        FINISHED,
        PAUSED;
        public boolean isActive(){
            return this==ACTIVE;
        }
    }

    @Getter
    private final LocalDateTime timestamp;
    @Getter
    private final State state;
}
