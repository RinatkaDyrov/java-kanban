package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public EpicHandler(TaskManager manager) {
        this.taskManager = manager;
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
            sendJson(exchange, taskManager.getAllEpics(), 200);
        } else if (path.length == 3) {
            int id = parseId(path[2]);
            if (id == -1) {
                sendText(exchange, "Invalid ID format", 400);
                return;
            }
            Optional<Epic> optionalEpic = taskManager.getEpicById(id);
            if (optionalEpic.isPresent()) {
                sendJson(exchange, optionalEpic.get(), 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange, String[] path) throws IOException {
        Epic epic = parseEpic(exchange);
        if (epic == null) {
            sendText(exchange, "Invalid epic data", 400);
            return;
        }
        if (path.length == 2) {
            try {
                if (taskManager.isJsonIdValid(epic.getId())) {
                    taskManager.createEpic(epic);
                    sendText(exchange, "Epic was added", 201);
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
            if (taskManager.getEpicById(id).isPresent()) {
                taskManager.updateEpic(epic);
                sendText(exchange, "Epic was updated", 201);

            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleDelete(HttpExchange exchange, String[] path) throws IOException {
        if (path.length == 2) {
            taskManager.deleteAllEpics();
            sendText(exchange, "All epics was deleted", 200);
        } else if (path.length == 3) {
            int id = parseId(path[2]);
            if (id == -1) {
                sendText(exchange, "Invalid ID format", 400);
                return;
            }
            if (taskManager.getEpicById(id).isPresent()) {
                taskManager.deleteEpicById(id);
                sendText(exchange, "Epic was deleted", 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private Epic parseEpic(HttpExchange exchange) throws IOException {
        InputStream body = exchange.getRequestBody();
        String jsonBody = new String(body.readAllBytes(), StandardCharsets.UTF_8);
        return gson.fromJson(jsonBody, Epic.class);
    }

    private int parseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
