import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

/**
 * Created by gagm2737 on 2015-07-07.
 */
@ToString
public class Experimentation {
    @Getter private final Scenario scenario;
    @Getter private final Collection<RawTask> rawTasks;
    @Getter private final List<ElaboratedTask> tasks;
    @Getter private final String name;
    @Getter private final String date;
    @Getter private final String time;

    public Experimentation(Scenario scenario, Collection<RawTask> rawTasks, List<ElaboratedTask> tasks){
        this.scenario = scenario;
        this.rawTasks = rawTasks;
        this.tasks = tasks;
        this.name = Utils.parseName(scenario.getName());
        this.date = Utils.parseDate(scenario.getStartTime());
        this.time = Utils.parseTime(scenario.getStartTime());
    }








}
