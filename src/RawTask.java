import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by ranj2004 on 15-06-30.
 */
@ToString
public class RawTask extends AbstractTask {
    @Getter private final ArrayList<Event> events = new ArrayList<>();

    public RawTask (int taskid) {
        super(taskid);
    }

    public ElaboratedTask elaborate(LocalDateTime scenarioStartTSP){
        ArrayList<Interval> segments = eventsToSegments(scenarioStartTSP);
        return new ElaboratedTask(taskid,segments);
    }

    private ArrayList<Interval> eventsToSegments(LocalDateTime scenarioStart){
        ArrayList<Interval> segments = new ArrayList<>();
        events.stream().reduce((firstEvent,lastEvent)->{
            if(firstEvent.getState().isActive()){
                if(!lastEvent.getState().isActive()){
                    Interval interval = new Interval(Utils.getLocalDuration(firstEvent.getTimestamp(),scenarioStart),Utils.getLocalDuration(lastEvent.getTimestamp(),scenarioStart));
                    segments.add(interval);
                } else {
                    throw new IllegalStateException("ACTIVE event cannot be followed by an ACTIVE event.");
                }
            }
            return lastEvent;
        });
        return segments;
    }
}
