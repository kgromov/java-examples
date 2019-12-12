package aws.time;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

//TODO: task analyzer: input = {UR, path1, path2}; output = {.sq3 file, temp json, max delta?}
/*
 * Input data: markets; [UR]; map1, map2; output dir
 * Container is initialized per market and processed parallel (phase 2);
 * 1) download (concurrently - phase 2) logs for map1 and map2;
 * 2) create .sq3 db file (name - market);
 * 3) find intersection of URs - each UR - 1 table;
 * 4) group tasks and insert into 2 columns
 * 5) Question: what to do if tasks differ?
 * Probably save build numbers (or map1/map2); group by task name
 */
@Getter
public class TaskTimeConsumption {
    private static final Comparator<TaskBean> TASK_COMPARATOR = Comparator.comparingDouble(TaskBean::getDuration).reversed()
            .thenComparing(TaskBean::getName);
    private static final Function<Path, String> PATH_TO_REGION = path ->
    {
        String fileName = path.getFileName().toString();
        int indexOfSuffix = fileName.indexOf("build_number");
        return indexOfSuffix != -1 ? fileName.substring(0, indexOfSuffix - 1) : fileName;
    };

    private final String region;
    private final List<TaskBean> tasks;
    private final AtomicDouble totalTime;

    public TaskTimeConsumption(String region) {
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

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);

        final int limit = 10;
        Path parentTurnOnFolder = Paths.get("C:\\HERE-CARDS\\my_dev_presubmits\\trash\\logs");
        Map<String, List<Path>> logsPerRegion = Files.find(parentTurnOnFolder,
                Short.MAX_VALUE,
                (filePath, fileAttr) -> fileAttr.isRegularFile() && filePath.toString().endsWith(".json"))
                .sorted(Comparator.comparing(o -> o.getFileName().toString()))
                .collect(Collectors.groupingBy(PATH_TO_REGION));

        logsPerRegion.forEach((region, logs) -> {
            System.out.println("\nRegion = " + region);
            for (Path log : logs) {

                try {
                    TaskTimeConsumption timeConsumption = new TaskTimeConsumption(log.toString());
                    timeConsumption.setTasks(log.toFile(), mapper);

                    Set<String> taskNames = timeConsumption.getTasks().stream()
                            .map(TaskBean::getName)
                            .collect(Collectors.toSet());
                    int tasksCount = timeConsumption.getTasks().size();
                    List<String> topLongTasks = timeConsumption.getTasks().stream().filter(t -> t.getName().contains("VanillaTask"))
                            .sorted(TASK_COMPARATOR)
                            .limit(limit)
                            .map(t -> String.format("%nTask = %s, duration = %s", t.getName(), t.getDuration()))
                            .collect(Collectors.toList());
                    System.out.println(String.format("LogFile = %s, total time = %s, top 10 tasks = %s",
                            log, timeConsumption.getTotalTime(), topLongTasks));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

}
