import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by ranj2004 on 15-06-30.
 */
@ToString
public class RawTask extends AbstractTask {
    @Getter
    private final ArrayList<Event> events = new ArrayList<>();

    public RawTask(int taskid) {
        super(taskid);
    }

    public ElaboratedTask elaborate(LocalDateTime scenarioStartTSP, LocalDateTime scenarioEndTSP) {
        ArrayList<Interval> segments = eventsToSegments(scenarioStartTSP, scenarioEndTSP);
        return new ElaboratedTask(taskid, segments, events);
    }

    private ArrayList<Interval> eventsToSegments(LocalDateTime scenarioStart, LocalDateTime scenarioEnd) {
        ArrayList<Interval> segments = new ArrayList<>();

        Optional<Event> testEvent = events.stream().reduce((firstEvent, lastEvent) -> {
            if (firstEvent.getState().isActive()) {
                if (!lastEvent.getState().isActive()) {
                    Interval interval = new Interval(Utils.getLocalDuration(firstEvent.getTimestamp(), scenarioStart), Utils.getLocalDuration(lastEvent.getTimestamp(), scenarioStart));
                    segments.add(interval);
                } else {
                    throw new IllegalStateException("ACTIVE event cannot be followed by an ACTIVE event.");
                }
            }
            return lastEvent;
        });

        testEvent.ifPresent((event -> {
            if (event.getState().isActive()) {
                Interval interval = new Interval(Utils.getLocalDuration(event.getTimestamp(), scenarioStart),
                        Utils.getLocalDuration(scenarioEnd, scenarioStart));
                segments.add(interval);
            }
        }));

        // version afonctionnelle, """"plus simple""""
/*        if(testEvent.isPresent()){
            if(testEvent.get().getState().isActive()){
                Interval interval = new Interval(Utils.getLocalDuration(testEvent.get().getTimestamp(), scenarioStart),
                        Utils.getLocalDuration(scenarioEnd, scenarioStart));
                segments.add(interval);
            }
        }*/

        return segments;
    }
}
