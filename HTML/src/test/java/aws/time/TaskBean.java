package aws.time;

import lombok.Data;

@Data
public class TaskBean {
    private final String name;
    private final String start;
    private final String end;
    private final double duration;
}
