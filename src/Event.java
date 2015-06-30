import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Created by ranj2004 on 15-06-30.
 */
@ToString
public class Event {
    public Event(String timestamp, State state) {
        this.timestamp = Utils.parseTimeStamp(timestamp);
        this.state = state;
    }

    enum State {
        ACTIVE,
        FINISHED,
        PAUSED
    }

    @Getter
    private final LocalDateTime timestamp;
    @Getter
    private final State state;
}
