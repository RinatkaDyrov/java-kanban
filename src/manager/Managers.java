package manager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        HistoryManager historyManager = getDefaultHistory();
        return new InMemoryTaskManager(historyManager);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getFileBackedTaskManager(File file) throws RuntimeException {
        HistoryManager historyManager = getDefaultHistory();
        if (file.exists()) {
            return new FileBackedTaskManager(historyManager, file);
        }
        throw new RuntimeException();
    }
}