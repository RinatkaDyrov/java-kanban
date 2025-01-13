package publicMethodsTests;

import manager.FileBackedTaskManager;
import manager.Managers;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class ThrowExceptionsTest {


    @Test
    void shouldNotThrowExceptionForInvalidFilePath() throws IOException {
        File invalidFile = Files.createTempFile("invalidFile","incorrectFormat").toFile();
        invalidFile.deleteOnExit();
        assertDoesNotThrow(() -> (Managers.getFileBackedTaskManager(invalidFile)));
    }

    @Test
    void shouldNotThrowExceptionForValidFilePath() {
        File validFile = new File("test.csv");
        assertThrows(RuntimeException.class, () -> Managers.getFileBackedTaskManager(validFile));
    }

    @Test
    void shouldThrowExceptionForOverlappingTasks() throws IOException{
        File tempFile = Files.createTempFile("tempFile", "csv").toFile();
        FileBackedTaskManager manager = Managers.getFileBackedTaskManager(tempFile);
        Task task = new Task("Task 1", "desc");
        Task overlappedTask = new Task("Task 2", "desc");
        LocalDateTime start = LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0);
        LocalDateTime start2 = LocalDateTime.of(2000, Month.JANUARY, 1, 1, 0);

        task.setStartTime(start);
        task.setDuration(Duration.ofHours(2));
        overlappedTask.setStartTime(start2);
        overlappedTask.setDuration(Duration.ofHours(2));

        manager.createTask(task);
        manager.createTask(overlappedTask);
        assertThrows(IllegalArgumentException.class, () -> manager.createTask(overlappedTask));
    }

    @Test
    void shouldThrowExceptionForOverlappingSubtasks() throws IOException{
        File tempFile = Files.createTempFile("tempFile", "csv").toFile();
        FileBackedTaskManager manager = Managers.getFileBackedTaskManager(tempFile);
//        Task task = new Task();
    }
}
