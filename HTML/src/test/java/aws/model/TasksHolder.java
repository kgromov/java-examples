package aws.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class TasksHolder {
    private static final Comparator<TaskBean> TASK_COMPARATOR = Comparator.comparingDouble(TaskBean::getDuration).reversed()
            .thenComparing(TaskBean::getName);

    private final String region;
    private final List<TaskBean> tasks;
    private final AtomicDouble totalTime;

    public TasksHolder(String region) {
        this.region = region;
        this.tasks = new ArrayList<>();
        this.totalTime = new AtomicDouble();
    }

    public void setTasks(File jsonFile, ObjectMapper mapper) throws IOException {
        JsonNode node = mapper.readTree(jsonFile);
        Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> childNode = iterator.next();
            String name = childNode.getKey();
            String start = childNode.getValue().get("Start").asText();
            String end = childNode.getValue().get("End").asText();
            double duration = childNode.getValue().get("duration").asDouble();
            TaskBean taskBean = new TaskBean(name, start, end, duration);
            tasks.add(taskBean);
            totalTime.addAndGet(duration);
        }
    }

    public Set<String> getTaskNames()
    {
        return tasks.stream().map(TaskBean::getName).collect(Collectors.toSet());
    }
}
