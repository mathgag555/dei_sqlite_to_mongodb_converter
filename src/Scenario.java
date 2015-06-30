import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Created by gagm2737 on 2015-06-30.
 */
@RequiredArgsConstructor
@ToString
public class Scenario {
    public Scenario(String name, String startTime, String endTime) {
        this.name = name;
        this.startTime_s = startTime;
        this.endTime_s = endTime;
        this.startTime = Utils.parseTimeStamp(startTime);
        this.endTime = Utils.parseTimeStamp(endTime);
    }

    @Getter private final String name;
    @Getter private final LocalDateTime startTime;
    @Getter private final String startTime_s;
    @Getter private final LocalDateTime endTime;
    @Getter private final String endTime_s;
}
