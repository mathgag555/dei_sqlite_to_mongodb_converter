import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranj2004 on 15-07-02.
 */
@ToString(callSuper = true)
public class ElaboratedTask extends AbstractTask {
    @Getter
    private final ArrayList<Interval> segments;
    @Getter
    private final List<Event> events;

    public ElaboratedTask(int taskid, ArrayList<Interval> segments, List<Event> events) {
        super(taskid);
        this.segments = segments;
        this.events = events;
    }

}
