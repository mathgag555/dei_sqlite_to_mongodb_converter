import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

/**
 * Created by ranj2004 on 15-06-30.
 */
@RequiredArgsConstructor
@ToString
public class RawTask {
    @Getter private final ArrayList<Event> events = new ArrayList<>();
    @Getter private final int taskid;

}
