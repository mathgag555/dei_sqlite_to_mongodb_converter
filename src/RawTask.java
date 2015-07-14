import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by ranj2004 on 15-06-30.
 */
@ToString
public class RawTask extends AbstractTask {


    private final List<Event> events = new ArrayList<>();

    public RawTask(int taskid) {
        super(taskid);
    }

    public ElaboratedTask elaborate(LocalDateTime scenarioStartTSP, LocalDateTime scenarioEndTSP) {
        ArrayList<Interval> segments = eventsToSegments(scenarioStartTSP, scenarioEndTSP);
        return new ElaboratedTask(taskid, segments, events);
    }

    public void addEvent(Event event){
        events.add(event);
    }

    public Collection<Event> getSortedEvents(){
        Collections.sort(events);
        return events;
    }

    private ArrayList<Interval> eventsToSegments(LocalDateTime scenarioStart, LocalDateTime scenarioEnd) {
        ArrayList<Interval> segments = new ArrayList<>();

        Optional<Event> testEvent = getSortedEvents().stream().reduce((firstEvent, lastEvent) -> {
            if (firstEvent.getState().isActive()) {
                if (!lastEvent.getState().isActive()) {
                    Interval interval = new Interval(Utils.getLocalDuration(scenarioStart, firstEvent.getTimestamp()), Utils.getLocalDuration(scenarioStart, lastEvent.getTimestamp()));
                    segments.add(interval);
                } else {
                    System.err.println("WARNING : ACTIVE event cannot be followed by an ACTIVE event.");
                }
            }
            return lastEvent;
        });

        testEvent.ifPresent((event -> {
            if (event.getState().isActive()) {
                Interval interval = new Interval(Utils.getLocalDuration(scenarioStart, event.getTimestamp()),
                        Utils.getLocalDuration(scenarioStart, scenarioEnd));
                segments.add(interval);
            }
        }));

        return segments;
    }
}
