import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Duration;

/**
 * Created by ranj2004 on 15-07-02.
 */
@ToString
@RequiredArgsConstructor
public class Interval {
    @Getter private final double start;
    @Getter private final double stop;
}
