package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager taskManager;
    Task task;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;


    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        task = new Task("Task 1", "Task 1 description", Status.NEW);
        taskManager.createTask(task);
        epic = new Epic("Epic 1", "Description of Epic 1");
        taskManager.createEpic(epic);
        subtask1 = new Subtask("Subtask 1", "Description subtask 1", Status.NEW, epic.getId());
        subtask2 = new Subtask("Subtask 2", "Description subtask 2", Status.NEW, epic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
    }

    @Test
    void shouldCreateAndAddAllTypesOfTasks(){
        assertEquals(task, taskManager.getTaskById(task.getId()));
        assertEquals(epic, taskManager.getEpicById(epic.getId()));
        assertEquals(subtask1, taskManager.getSubtaskById(subtask1.getId()));
        assertEquals(subtask2, taskManager.getSubtaskById(subtask2.getId()));
    }

    @Test
    void shouldReturnNotEqualsByComparingUpdatedTaskWithHistoryTask_2(){
        taskManager.createTask(task);

        Task calledTask = taskManager.getTaskById(task.getId());
        Task taskFromHistory = taskManager.getHistory().getFirst();
        assertEquals(calledTask, taskFromHistory);

        task.setName("Updated name for task");
        taskManager.updateTask(task);
        Task updatedTask = taskManager.getTaskById(task.getId());

        assertNotEquals(updatedTask, taskFromHistory);
    }
}