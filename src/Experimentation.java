import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Getter private final double durationInMins;

    public Experimentation(Scenario scenario, Collection<RawTask> rawTasks, List<ElaboratedTask> tasks){
        this.scenario = scenario;
        this.rawTasks = rawTasks;
        this.tasks = tasks;
        this.name = parseName(scenario.getName());
        this.date = Utils.parseDate(scenario.getStartTime());
        this.time = Utils.parseTime(scenario.getStartTime());
        this.durationInMins = scenario.getDurationInMins();
    }

    public static String parseName(String name){
        Pattern pattern = Pattern.compile("^.*(EXP\\w+)$");
        Matcher matcher = pattern.matcher(name);
        matcher.matches();
        return matcher.group(1);
    }
}
