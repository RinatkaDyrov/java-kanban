package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private int taskID = 1;
    private final static HashMap<Integer, Task> taskList = new HashMap<>();
    private final static HashMap<Integer, Epic> epicList = new HashMap<>();
    private final static HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskList.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskList.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicList.values());
    }

    @Override
    public void deleteAllTasks() {
        taskList.keySet().forEach(historyManager::remove);
        taskList.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtaskList.keySet().forEach(historyManager::remove);
        subtaskList.clear();
        epicList.values().forEach(Epic::deleteAllSubtasks);
    }

    @Override
    public void deleteAllEpics() {
        epicList.keySet().forEach(historyManager::remove);
        epicList.values().forEach(Epic::deleteAllSubtasks);
        epicList.clear();
        deleteAllSubtasks();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = taskList.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicList.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtaskList.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void createTask(Task task) {
        taskList.put(taskID, task);
        task.setId(taskID);
        taskID++;
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtaskList.put(taskID, subtask);
        epicList.get(subtask.getEpicId()).addSubtask(subtask);
        subtask.setId(taskID);
        taskID++;
    }

    @Override
    public void createEpic(Epic epic) {
        epicList.put(taskID, epic);
        epic.setId(taskID);
        taskID++;
    }

    @Override
    public void updateTask(Task task) {
        taskList.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicList.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskList.put(subtask.getId(), subtask);
    }

    @Override
    public void deleteTaskById(int id) {
        taskList.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        epicList.get(id).getSubtasks().forEach(subtask -> {
            historyManager.remove(subtask.getId());
            subtaskList.remove(subtask.getId());
        });
        epicList.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subForDelete = getSubtaskById(id);
        epicList.get(subForDelete.getEpicId()).deleteSubtask(subForDelete);
        subtaskList.remove(id);
        historyManager.remove(id);
    }

    @Override
    public ArrayList<Subtask> getAllSubtaskByEpic(int epicId) {
        return epicList.get(epicId).getSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}