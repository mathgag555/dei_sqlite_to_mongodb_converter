import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Created by ranj2004 on 15-07-02.
 */
@RequiredArgsConstructor
@ToString
public abstract class AbstractTask {
    @Getter
    protected final int taskid;

}
