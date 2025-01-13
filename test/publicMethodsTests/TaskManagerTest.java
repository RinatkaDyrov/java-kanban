package publicMethodsTests;

import manager.TaskManager;
import model.Task;
import model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    abstract T createTaskManager() throws IOException;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = createTaskManager();
    }

    @Test
    void shouldAddTask() {
        Task task = new Task("Task", "Description", Status.NEW);
        taskManager.createTask(task);

        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(task, taskManager.getTaskById(task.getId()).get());
    }

    @Test
    void shouldRemoveTask() {
        Task task = new Task("Task", "Description", Status.NEW);
        taskManager.createTask(task);
        taskManager.deleteTaskById(task.getId());

        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void shouldUpdateTask() {
        Task task = new Task("Task", "Description", Status.NEW);
        taskManager.createTask(task);

        task.setName("Updated Task");
        taskManager.updateTask(task);

        assertEquals("Updated Task", taskManager.getTaskById(task.getId()).get().getName());
    }

    // Добавить тесты для пересечения времени.
}
