package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    void shouldCreateAndAddAllTypesOfTasks() {
        Task taskById = taskManager.getTaskById(task.getId()).isPresent() ?
                taskManager.getTaskById(task.getId()).get() : null;
        Epic epicById = taskManager.getEpicById(epic.getId()).isPresent() ?
                taskManager.getEpicById(epic.getId()).get() : null;
        Task subById1 = taskManager.getSubtaskById(subtask1.getId()).isPresent() ?
                taskManager.getSubtaskById(subtask1.getId()).get() : null;
        Task subById2 = taskManager.getSubtaskById(subtask2.getId()).isPresent() ?
                taskManager.getSubtaskById(subtask2.getId()).get() : null;
        assertNotNull(taskById);
        assertNotNull(epicById);
        assertNotNull(subById1);
        assertNotNull(subById2);
        assertEquals(task, taskById);
        assertEquals(epic, epicById);
        assertEquals(subtask1, subById1);
        assertEquals(subtask2, subById2);
    }

    @Test
    void shouldReturnEqualsByComparingUpdatedTaskWithHistoryTask() {
        task.setName("Updated name for task");
        taskManager.updateTask(task);

        Task updatedTask = taskManager.getTaskById(task.getId()).isPresent() ? taskManager.getTaskById(task.getId()).get() : null;
        assertNotNull(updatedTask);
        Task taskFromHistory = taskManager.getHistory().getFirst();

        assertEquals(updatedTask, taskFromHistory);
    }


    @Test
    void shouldAddTaskToHistory() {
        Task newTask = new Task("New Task", "New task description", Status.NEW);
        taskManager.createTask(newTask);
        taskManager.getTaskById(newTask.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(newTask, history.getFirst());
    }


    @Test
    void shouldRemoveTaskFromHistory() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        task1.setId(1);
        taskManager.createTask(task1);
        taskManager.getTaskById(task1.getId());

        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        task2.setId(2);
        taskManager.createTask(task2);
        taskManager.getTaskById(task2.getId());

        taskManager.deleteTaskById(task1.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.getFirst());
    }

    @Test
    void shouldUpdateTaskInHistory() {
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        task.setId(1);
        taskManager.createTask(task);
        taskManager.getTaskById(task.getId());

        task.setName("Updated Task 1");
        taskManager.updateTask(task);
        taskManager.getTaskById(task.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals("Updated Task 1", history.getFirst().getName());
    }

    @Test
    void shouldRemoveSubtaskIdFromEpicWhenSubtaskRemoved() {
        Subtask subtaskToRemove = subtask1;
        taskManager.deleteSubtaskById(subtaskToRemove.getId());

        List<Subtask> subtasks = taskManager.getAllSubtaskByEpic(epic.getId());
        assertFalse(subtasks.contains(subtaskToRemove));
    }

    @Test
    void shouldUpdateTaskNameWithSetter() {
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        task.setId(1);
        taskManager.createTask(task);
        taskManager.getTaskById(task.getId());

        task.setName("Updated Task 1");
        taskManager.updateTask(task);

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals("Updated Task 1", history.getFirst().getName());
    }

    @Test
    void shouldUpdateTaskStatusWithSetter() {
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        task.setId(1);
        taskManager.createTask(task);
        taskManager.getTaskById(task.getId());

        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(Status.IN_PROGRESS, history.get(0).getStatus());
    }
}