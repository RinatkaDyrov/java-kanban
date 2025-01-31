package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
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
    File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("testFile", ".csv");
        tempFile.deleteOnExit();
        manager = Managers.getFileBackedTaskManager(tempFile);
        manager.clearAll();     // Добавлена очистка менеджера, так как все тесты выполнялись поотдельности, но при
        // запуске всех подряд, происходила какая-то утечка. shouldSaveAndLoadEmptyFile и shouldSaveAndLoadEmptyFile
        // переставали выполняться
    }

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
        FileBackedTaskManager saveManager = Managers.getFileBackedTaskManager(tempFile);
        saveManager.createTask(new Task("New Task", "Description", Status.NEW));

        FileBackedTaskManager loadManager = Managers.getFileBackedTaskManager(tempFile);

        assertEquals(saveManager.getAllTasks(), loadManager.getAllTasks());
        assertEquals(saveManager.getAllEpics(), loadManager.getAllEpics());
        assertEquals(saveManager.getAllSubtasks(), loadManager.getAllSubtasks());
        tempFile.deleteOnExit();
    }


    //Проверка старого функционала
    @Test
    void shouldCreateAndAddAllTypesOfTasks() throws IOException {
        presetForManager();
        Task taskById = manager.getTaskById(task.getId()).isPresent() ? manager.getTaskById(task.getId()).get() : null;
        Task epicById = manager.getEpicById(epic.getId()).isPresent() ? manager.getEpicById(epic.getId()).get() : null;
        Task subtaskById1 = manager.getSubtaskById(subtask1.getId()).isPresent() ? manager.getSubtaskById(subtask1.getId()).get() : null;
        Task subtaskById2 = manager.getSubtaskById(subtask2.getId()).isPresent() ? manager.getSubtaskById(subtask2.getId()).get() : null;
        assertEquals(task, taskById);
        assertEquals(epic, epicById);
        assertEquals(subtask1, subtaskById1);
        assertEquals(subtask2, subtaskById2);
    }

    @Test
    void shouldReturnEqualsByComparingUpdatedTaskWithHistoryTask() throws IOException {
        presetForManager();
        task.setName("Updated name for task");
        manager.updateTask(task);

        Task updatedTask = manager.getTaskById(task.getId()).get();
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
