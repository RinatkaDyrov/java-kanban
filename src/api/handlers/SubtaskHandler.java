package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
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
    }

    private void handleGet(HttpExchange exchange, String[] path) throws IOException {
        if (path.length == 2) {
            sendJson(exchange, taskManager.getAllSubtasks(), 200);
        } else if (path.length == 3) {
            int id = parseId(path[2]);
            if (id == -1) {
                sendText(exchange, "Invalid ID format", 404);
            }
            Optional<Subtask> optionalSubtask = taskManager.getSubtaskById(id);
            if (optionalSubtask.isPresent()) {
                sendJson(exchange, optionalSubtask, 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange, String[] path) throws IOException {
        Subtask subtask = parseSubtask(exchange);
        if (subtask == null) {
            sendText(exchange, "Invalid subtask data", 400);
        }
        if (path.length == 2) {
            try {
                if (taskManager.isJsonIdValid(subtask.getId())) {
                    taskManager.createSubtask(subtask);
                    sendText(exchange, "Subtask was added", 201);
                } else {
                    sendText(exchange, "Json body contains invalid ID data", 406);
                }
            } catch (IllegalArgumentException e) {
                sendHasInteractions(exchange);
            }
        } else if (path.length == 3) {
            int id = parseId(path[2]);
            if (id == -1) {
                sendText(exchange, "Invalid ID format", 404);
            }
            if (taskManager.getSubtaskById(id).isPresent()) {
                taskManager.updateSubtask(subtask);
                sendText(exchange, "Subtask was updated", 201);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleDelete(HttpExchange exchange, String[] path) throws IOException {
        if (path.length == 2) {
            taskManager.deleteAllSubtasks();
            sendText(exchange, "All subtasks was deleted", 200);
        } else if (path.length == 3) {
            int id = parseId(path[2]);
            if (id == -1) {
                sendText(exchange, "Invalid ID format", 400);
                return;
            }
            if (taskManager.getSubtaskById(id).isPresent()) {
                taskManager.deleteSubtaskById(id);
                sendText(exchange, "Subtask was deleted", 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private Subtask parseSubtask(HttpExchange exchange) throws IOException {
        InputStream body = exchange.getRequestBody();
        String jsonBody = new String(body.readAllBytes(), StandardCharsets.UTF_8);
        return gson.fromJson(jsonBody, Subtask.class);
    }

    private int parseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
