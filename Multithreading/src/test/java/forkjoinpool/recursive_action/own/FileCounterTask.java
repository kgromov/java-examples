package forkjoinpool.recursive_action.own;

import java.io.File;
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
        for (File file : files) {
            if (file.isFile()) {
                ++filesCount;
            } else {
                filesCount +=new FileCounterTask(file).fork().join();
            }
        }
        return filesCount;
    }
}
