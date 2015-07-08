import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

/**
 * Created by gagm2737 on 2015-07-07.
 */
@ToString
@RequiredArgsConstructor
public class Experimentation {
    @Getter private final Scenario scenario;
    @Getter private final Collection<RawTask> rawTasks;
    @Getter private final List<ElaboratedTask> tasks;




}
