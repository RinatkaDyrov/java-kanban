package api;

import api.typeAdapters.DurationAdapter;
import api.typeAdapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {

    TaskManager manager;
    HttpTaskServer taskServer;
    Gson gson;

    public HttpTaskManagerTasksTest() {
        manager = Managers.getDefault();
        taskServer = new HttpTaskServer(manager);
        gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @BeforeEach
    public void setUp() throws IOException {
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
        taskServer.startServer(8080);
    }

    @AfterEach
    public void shutDown() {
        taskServer.stopServer();
    }

    @Test
    public void shouldAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Test desc", Status.NEW);
        task.setId(1);

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Expected status code 201");

        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager, "Tasks should not be null");
        assertEquals(1, tasksFromManager.size(), "Expected 1 task, but found " + tasksFromManager.size());
        assertEquals("Test Task", tasksFromManager.getFirst().getName(), "Task name is incorrect");
    }

    @Test
    public void shouldAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test epic", "Epic desc");
        epic.setId(1);
        String epicJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Expected status code 201");

        List<Epic> epicsFromManager = manager.getAllEpics();
        assertNotNull(epicsFromManager, "Epics should not be null");
        assertEquals(1, epicsFromManager.size(), "Expected 1 epic, but found " + epicsFromManager.size());
        assertEquals("Test epic", epicsFromManager.getFirst().getName(), "Epic name is incorrect");
    }

    @Test
    public void shouldAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic name", "Epic desc");
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Sub1 name", "Sub1 desc", Status.NEW, epic.getId());
        subtask.setId(2);
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ofMinutes(20));
        String subtaskJson = gson.toJson(subtask, Subtask.class);
        System.out.println(subtaskJson);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Expected status code 201");

        List<Subtask> subtasksFromManager = manager.getAllSubtasks();
        assertNotNull(subtasksFromManager, "Subtasks should not be null");
        assertEquals(1, subtasksFromManager.size(), "Expected 1 subtask, but found " + subtasksFromManager.size());
        assertEquals("Sub1 name", subtasksFromManager.getFirst().getName(), "Subtask name is incorrect");
    }

    @Test
    public void shouldGetAllTypesOfTasks() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Test desc", Status.NEW);
        manager.createTask(task);
        Epic epic = new Epic("Epic name", "Epic desc");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Sub1 name", "Sub1 desc", Status.NEW, epic.getId());
        manager.createSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI urlTasks = URI.create("http://localhost:8080/tasks");
        URI urlEpics = URI.create("http://localhost:8080/epics");
        URI urlSubtasks = URI.create("http://localhost:8080/subtasks");

        HttpRequest requestForTasks = HttpRequest.newBuilder()
                .uri(urlTasks)
                .GET()
                .build();
        HttpResponse<String> responseGetTasks = client.send(requestForTasks, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetTasks.statusCode(), "Expected status code 200");

        HttpRequest requestForEpics = HttpRequest.newBuilder()
                .uri(urlEpics)
                .GET()
                .build();
        HttpResponse<String> responseGetEpics = client.send(requestForEpics, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetEpics.statusCode(), "Expected status code 200");

        HttpRequest requestForSubtasks = HttpRequest.newBuilder()
                .uri(urlSubtasks)
                .GET()
                .build();
        HttpResponse<String> responseGetSubtasks = client.send(requestForSubtasks, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetSubtasks.statusCode(), "Expected status code 200");
    }

    @Test
    public void shouldDeleteAllTypesOfTasks() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Test desc", Status.NEW);
        manager.createTask(task);
        Epic epic = new Epic("Epic name", "Epic desc");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Sub1 name", "Sub1 desc", Status.NEW, epic.getId());
        manager.createSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI urlTasks = URI.create("http://localhost:8080/tasks/1");
        URI urlEpics = URI.create("http://localhost:8080/epics/2");
        URI urlSubtasks = URI.create("http://localhost:8080/subtasks/3");

        HttpRequest requestForTasks = HttpRequest.newBuilder()
                .uri(urlTasks)
                .DELETE()
                .build();
        HttpResponse<String> responseGetTasks = client.send(requestForTasks, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetTasks.statusCode(), "Expected status code 200");
        assertEquals(0, manager.getAllTasks().size());

        HttpRequest requestForSubtasks = HttpRequest.newBuilder()
                .uri(urlSubtasks)
                .DELETE()
                .build();
        HttpResponse<String> responseGetSubtasks = client.send(requestForSubtasks, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetSubtasks.statusCode(), "Expected status code 200");
        assertEquals(0, manager.getAllSubtasks().size());

        HttpRequest requestForEpics = HttpRequest.newBuilder()
                .uri(urlEpics)
                .DELETE()
                .build();
        HttpResponse<String> responseGetEpics = client.send(requestForEpics, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetEpics.statusCode(), "Expected status code 200");
        assertEquals(0, manager.getAllEpics().size());
    }

    @Test
    public void shouldReturnHistoryAndPrioritizedTasks() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Test desc", Status.NEW);
        manager.createTask(task);
        Epic epic = new Epic("Epic name", "Epic desc");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Sub1 name", "Sub1 desc", Status.NEW, epic.getId());
        manager.createSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI history = URI.create("http://localhost:8080/history");
        URI prioritized = URI.create("http://localhost:8080/prioritized");

        HttpRequest requestForHistory = HttpRequest.newBuilder()
                .uri(history)
                .GET()
                .build();
        HttpResponse<String> responseGetHistory = client.send(requestForHistory, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetHistory.statusCode(), "Expected status code 200");

        HttpRequest requestForPrioritized = HttpRequest.newBuilder()
                .uri(prioritized)
                .GET()
                .build();
        HttpResponse<String> responseGetPrioritized = client.send(requestForPrioritized, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetPrioritized.statusCode(), "Expected status code 200");
        assertEquals(0, manager.getHistory().size());
        assertEquals(3, manager.getPrioritizedTasks().size());

        HttpRequest someGet1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();

        HttpRequest someGet2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/2"))
                .GET()
                .build();
        HttpResponse<String> responseSomeGet1 = client.send(someGet1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseSomeGet1.statusCode());
        HttpResponse<String> responseSomeGet2 = client.send(someGet2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseSomeGet2.statusCode());

        HttpResponse<String> responseGetAnotherPrioritized = client.send(requestForPrioritized, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetAnotherPrioritized.statusCode(), "Expected status code 200");
        assertEquals(2, manager.getHistory().size());
        assertEquals(3, manager.getPrioritizedTasks().size());
    }
}