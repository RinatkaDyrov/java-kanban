package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    FileBackedTaskManager manager;
    Task task;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;

    @Test
    void shouldSaveAndLoadEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("emptyFile", ".csv");
        emptyFile.deleteOnExit();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(emptyFile))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();
        }

        FileBackedTaskManager emptyManager = Managers.getFileBackedTaskManager(emptyFile);

        assertTrue(emptyManager.getAllTasks().isEmpty(), "Tasks list should be empty");
        assertTrue(emptyManager.getAllEpics().isEmpty(), "Epics list should be empty");
        assertTrue(emptyManager.getAllSubtasks().isEmpty(), "Subtasks list should be empty");
    }


    @Test
    void shouldSaveAndLoadData() throws IOException {
        File tempFile = File.createTempFile("dataFile", ".csv");
        tempFile.deleteOnExit();

        FileBackedTaskManager saveManager = Managers.getFileBackedTaskManager(tempFile);
        saveManager.createTask(new Task("New Task", "Description", Status.NEW));

        FileBackedTaskManager loadManager = Managers.getFileBackedTaskManager(tempFile);

        assertEquals(saveManager.getAllTasks(), loadManager.getAllTasks());
        assertEquals(saveManager.getAllEpics(), loadManager.getAllEpics());
        assertEquals(saveManager.getAllSubtasks(), loadManager.getAllSubtasks());
    }


    //Проверка старого функционала
    @Test
    void shouldCreateAndAddAllTypesOfTasks() throws IOException {
        presetForManager();
        assertEquals(task, manager.getTaskById(task.getId()));
        assertEquals(epic, manager.getEpicById(epic.getId()));
        assertEquals(subtask1, manager.getSubtaskById(subtask1.getId()));
        assertEquals(subtask2, manager.getSubtaskById(subtask2.getId()));
    }

    @Test
    void shouldReturnEqualsByComparingUpdatedTaskWithHistoryTask() throws IOException {
        presetForManager();
        task.setName("Updated name for task");
        manager.updateTask(task);

        Task updatedTask = manager.getTaskById(task.getId());
        Task taskFromHistory = manager.getHistory().getFirst();

        assertEquals(updatedTask, taskFromHistory);
    }


    @Test
    void shouldAddTaskToHistory() throws IOException {
        presetForManager();
        Task newTask = new Task("New Task", "New task description", Status.NEW);
        manager.createTask(newTask);
        manager.getTaskById(newTask.getId());

        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals(newTask, history.getFirst());
    }


    @Test
    void shouldRemoveTaskFromHistory() throws IOException {
        presetForManager();
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        task1.setId(1);
        manager.createTask(task1);
        manager.getTaskById(task1.getId());

        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        task2.setId(2);
        manager.createTask(task2);
        manager.getTaskById(task2.getId());

        manager.deleteTaskById(task1.getId());

        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.getFirst());
    }

    @Test
    void shouldUpdateTaskInHistory() throws IOException {
        presetForManager();
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        task.setId(1);
        manager.createTask(task);
        manager.getTaskById(task.getId());

        task.setName("Updated Task 1");
        manager.updateTask(task);
        manager.getTaskById(task.getId());

        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals("Updated Task 1", history.getFirst().getName());
    }

    @Test
    void shouldRemoveSubtaskIdFromEpicWhenSubtaskRemoved() throws IOException {
        presetForManager();
        Subtask subtaskToRemove = subtask1;
        manager.deleteSubtaskById(subtaskToRemove.getId());

        List<Subtask> subtasks = manager.getAllSubtaskByEpic(epic.getId());
        assertFalse(subtasks.contains(subtaskToRemove));
    }

    @Test
    void shouldUpdateTaskNameWithSetter() throws IOException {
        presetForManager();
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        task.setId(1);
        manager.createTask(task);
        manager.getTaskById(task.getId());

        task.setName("Updated Task 1");
        manager.updateTask(task);

        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals("Updated Task 1", history.getFirst().getName());
    }

    @Test
    void shouldUpdateTaskStatusWithSetter() throws IOException {
        presetForManager();
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        task.setId(1);
        manager.createTask(task);
        manager.getTaskById(task.getId());

        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);

        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals(Status.IN_PROGRESS, history.getFirst().getStatus());
    }

    void presetForManager() throws IOException {
        File tempFile = File.createTempFile("tempFile", ".csv");
        tempFile.deleteOnExit();
        manager = Managers.getFileBackedTaskManager(tempFile);

        task = new Task("Task 1", "Task 1 description", Status.NEW);
        manager.createTask(task);

        epic = new Epic("Epic 1", "Description of Epic 1");
        manager.createEpic(epic);

        subtask1 = new Subtask("Subtask 1", "Description subtask 1", Status.NEW, epic.getId());
        subtask2 = new Subtask("Subtask 2", "Description subtask 2", Status.NEW, epic.getId());
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
    }
}
