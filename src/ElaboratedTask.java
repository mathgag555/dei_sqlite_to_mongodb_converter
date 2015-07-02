import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

/**
 * Created by ranj2004 on 15-07-02.
 */
@ToString(callSuper = true)
public class ElaboratedTask extends AbstractTask {
    @Getter
    private final ArrayList<Interval> segments;

    public ElaboratedTask(int taskid,ArrayList<Interval> segments){
        super(taskid);
        this.segments=segments;
    }

}
