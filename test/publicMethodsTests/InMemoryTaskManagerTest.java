package publicMethodsTests;

import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    InMemoryTaskManager createTaskManager() {
        InMemoryTaskManager manager = (InMemoryTaskManager) Managers.getDefault();
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
