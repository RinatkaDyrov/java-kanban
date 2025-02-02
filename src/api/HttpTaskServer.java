package api;

import api.handlers.EpicHandler;
import api.handlers.HistoryHandler;
import api.handlers.SubtaskHandler;
import api.handlers.TaskHandler;
import api.handlers.PrioritizedTasksHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private HttpServer server;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void startServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/tasks", new TaskHandler(taskManager));
        server.createContext("/subtasks", new SubtaskHandler(taskManager));
        server.createContext("/epics", new EpicHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedTasksHandler(taskManager));
        server.start();
        System.out.println("Сервер запущен на порту " + port);
    }

    public void stopServer() {
        if (server != null) {
            server.stop(0);
            System.out.println("Сервер остановлен.");
        }
    }

    public static void main(String[] args) throws IOException {

        HttpTaskServer server = new HttpTaskServer(Managers.getDefault());
        server.startServer(8080);
    }
}
