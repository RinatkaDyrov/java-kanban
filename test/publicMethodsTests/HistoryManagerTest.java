package publicMethodsTests;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void shouldReturnEmptyHistoryWhenNoTasks() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void shouldNotAddDuplicateTasks() {
        Task task = new Task("Task", "Description", Status.NEW);
        historyManager.add(task);
        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        Task task1 = new Task("Task1", "Description1", Status.NEW);
        Task task2 = new Task("Task2", "Description2", Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task1.getId());
        assertFalse(historyManager.getHistory().contains(task1));
    }
}
