package publicMethodsTests;

import manager.FileBackedTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileBackedTaskManagerTest extends TaskManagerTest {

    @Override
    FileBackedTaskManager createTaskManager() {
        File tempFile = null;
        try {
            tempFile = Files.createTempFile("tempFile", "csv").toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert tempFile != null;
        FileBackedTaskManager manager = Managers.getFileBackedTaskManager(tempFile);
        manager.clearAll();
        return manager;
    }

    @Test
    @Override
    void shouldAddTask() {
        super.shouldAddTask();
    }

    @Test
    @Override
    void shouldRemoveTask() {
        super.shouldRemoveTask();
    }

    @Test
    @Override
    void shouldUpdateTask() {
        super.shouldUpdateTask();
    }
}
