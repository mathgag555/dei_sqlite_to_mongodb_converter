import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Created by ranj2004 on 15-06-30.
 */
@RequiredArgsConstructor
@ToString
public class Event {
    enum State {
        ACTIVE,
        FINISHED,
        PAUSED
    }


    @Getter
    private final String timestamp;
    @Getter
    private final State state;
}
