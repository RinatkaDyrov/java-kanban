package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equalsIgnoreCase("GET")) {
            sendJson(exchange, taskManager.getHistory(), 200);
        } else {
            sendText(exchange, "Method not allowed", 405);
        }
    }
}
