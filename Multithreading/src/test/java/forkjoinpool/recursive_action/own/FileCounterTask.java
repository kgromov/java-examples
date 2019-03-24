package forkjoinpool.recursive_action.own;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Created by konstantin on 24.03.2019.
 */
public class FileCounterTask extends RecursiveTask<Integer> {
    private File directoryToBypass;

    public FileCounterTask(File directoryToBypass) {
        this.directoryToBypass = directoryToBypass;
    }

    @Override
    protected Integer compute() {
        File[] files = directoryToBypass.listFiles();
        if (files == null) {
            return 0;
        }
        int filesCount = 0;
        List<FileCounterTask> childTasks = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                ++filesCount;
            } else {
//                childTasks.add(new FileCounterTask(file));
                filesCount +=new FileCounterTask(file).fork().join();
            }
        }
//        return filesCount + childTasks.stream().peek(task -> fork()).mapToInt(ForkJoinTask::join).sum();
        return filesCount;
    }
}
