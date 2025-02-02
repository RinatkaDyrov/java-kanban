package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] path = exchange.getRequestURI().getPath().split("/");
        switch (method) {
            case "GET":
                System.out.println("Получен GET-запрос");
                handleGet(exchange, path);
                System.out.println("Запрос обработан");
                break;
            case "POST":
                System.out.println("Получен POST-запрос");
                handlePost(exchange, path);
                System.out.println("Запрос обработан");
                break;
            case "DELETE":
                System.out.println("Получен DELETE-запрос");
                handleDelete(exchange, path);
                System.out.println("Запрос обработан");
                break;
            default:
                sendText(exchange, "Method not allowed", 405);
        }
        exchange.close();
    }

    private void handleGet(HttpExchange exchange, String[] path) throws IOException {
        if (path.length == 2) {
            sendJson(exchange, taskManager.getAllTasks(), 200);
        } else if (path.length == 3) {
            int id = parseId(path[2]);
            if (id == -1) {
                sendText(exchange, "Invalid ID format", 400);
                return;
            }
            Optional<Task> optionalTask = taskManager.getTaskById(id);
            if (optionalTask.isPresent()) {
                sendJson(exchange, optionalTask.get(), 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange, String[] path) throws IOException {
        Task task = parseTask(exchange);
        if (task == null) {
            sendText(exchange, "Invalid task data", 400);
            return;
        }
        if (path.length == 2) {
            try {
                if (taskManager.isJsonIdValid(task.getId())) {
                    taskManager.createTask(task);
                    sendText(exchange, "Task was added", 201);
                } else {
                    sendText(exchange, "Json body contains invalid ID data", 406);
                }
            } catch (IllegalArgumentException e) {
                sendHasInteractions(exchange);
            }
        } else if (path.length == 3) {
            int id = parseId(path[2]);
            if (id == -1) {
                sendText(exchange, "Invalid ID format", 400);
                return;
            }
            if (taskManager.getTaskById(id).isPresent()) {
                task.setId(id);
                taskManager.updateTask(task);
                sendText(exchange, "Task was updated", 201);

            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleDelete(HttpExchange exchange, String[] path) throws IOException {
        if (path.length == 2) {
            taskManager.deleteAllTasks();
            sendText(exchange, "All tasks was deleted", 200);
        } else if (path.length == 3) {
            int id = parseId(path[2]);
            if (id == -1) {
                sendText(exchange, "Invalid ID format", 400);
                return;
            }
            if (taskManager.getTaskById(id).isPresent()) {
                taskManager.deleteTaskById(id);
                sendText(exchange, "Task was deleted", 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private Task parseTask(HttpExchange exchange) throws IOException {
        InputStream body = exchange.getRequestBody();
        String jsonBody = new String(body.readAllBytes(), StandardCharsets.UTF_8);
        return gson.fromJson(jsonBody, Task.class);
    }

    private int parseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
