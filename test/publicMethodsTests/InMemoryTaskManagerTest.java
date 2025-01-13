package publicMethodsTests;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    InMemoryTaskManager createTaskManager() {
        InMemoryTaskManager manager = (InMemoryTaskManager) Managers.getDefault();
        manager.clearAll();
        return manager; // Создаём тестовый экземпляр InMemoryTaskManager
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
