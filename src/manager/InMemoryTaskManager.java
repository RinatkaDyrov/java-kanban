package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskType;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final HistoryManager historyManager;
    private final TreeSet<Task> sortedByPriorityTasks;
    Comparator<Task> comparator = Comparator.nullsLast
            (Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Task::getDuration, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Task::getId));

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        sortedByPriorityTasks = new TreeSet<>(comparator);
    }

    private int taskID = 1;
    private static final HashMap<Integer, Task> taskList = new HashMap<>();
    private static final HashMap<Integer, Epic> epicList = new HashMap<>();
    private static final HashMap<Integer, Subtask> subtaskList = new HashMap<>();

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
        sortedByPriorityTasks.removeIf(task -> task.getType() == TaskType.TASK);
    }

    @Override
    public void deleteAllSubtasks() {
        subtaskList.keySet().forEach(historyManager::remove);
        subtaskList.clear();
        epicList.values().forEach(Epic::deleteAllSubtasks);
        sortedByPriorityTasks.removeIf(task -> task.getType() == TaskType.SUBTASK);

    }

    @Override
    public void deleteAllEpics() {
        epicList.keySet().forEach(historyManager::remove);
        epicList.values().forEach(Epic::deleteAllSubtasks);
        epicList.clear();
        deleteAllSubtasks();
        sortedByPriorityTasks.removeIf(task -> task.getType() == TaskType.EPIC || task.getType() == TaskType.SUBTASK);
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        Task task = taskList.get(id);
        historyManager.add(task);
        return Optional.ofNullable(task);
    }

    @Override
    public Optional<Epic> getEpicById(int id) {
        Epic epic = epicList.get(id);
        historyManager.add(epic);
        return Optional.ofNullable(epic);
    }

    @Override
    public Optional<Subtask> getSubtaskById(int id) {
        Subtask subtask = subtaskList.get(id);
        historyManager.add(subtask);
        return Optional.ofNullable(subtask);
    }

    @Override
    public void createTask(Task task) {
        if (isTaskTimeOverlapping(task)) {
            throw new IllegalArgumentException("Новая задача пересекается с существующей!");
        }
        taskList.put(taskID, task);
        sortedByPriorityTasks.add(task);
        task.setId(taskID);
        taskID++;
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (isTaskTimeOverlapping(subtask)) {
            throw new IllegalArgumentException("Новая задача пересекается с существующей!");
        }
        subtaskList.put(taskID, subtask);
        sortedByPriorityTasks.add(subtask);
        epicList.get(subtask.getEpicId()).addSubtask(subtask);
        subtask.setId(taskID);
        taskID++;
    }

    @Override
    public void createEpic(Epic epic) {
        epicList.put(taskID, epic);
        sortedByPriorityTasks.add(epic);
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
        sortedByPriorityTasks.remove(taskList.get(id));
        taskList.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        epicList.get(id).getSubtasks().forEach(subtask -> {
            historyManager.remove(subtask.getId());
            sortedByPriorityTasks.remove(subtask);
            subtaskList.remove(subtask.getId());
        });
        sortedByPriorityTasks.remove(epicList.get(id));
        epicList.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subForDelete = getSubtaskById(id).isPresent() ? getSubtaskById(id).get() : null;
        assert subForDelete != null;
        sortedByPriorityTasks.remove(subForDelete);
        epicList.get(subForDelete.getEpicId()).deleteSubtask(subForDelete);
        subtaskList.remove(id);
        historyManager.remove(id);
    }

    public void clearAll() {
        deleteAllTasks();
        deleteAllEpics();
        deleteAllSubtasks();
        sortedByPriorityTasks.clear();
    }

    @Override
    public ArrayList<Subtask> getAllSubtaskByEpic(int epicId) {
        return epicList.get(epicId).getSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public Set<Task> getPrioritizedTasks() {
        return sortedByPriorityTasks;
    }

    private boolean isOverlapping(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }
        LocalDateTime task1Start = task1.getStartTime();
        LocalDateTime task1End = task1Start.plus(task1.getDuration());
        LocalDateTime task2Start = task2.getStartTime();
        LocalDateTime task2End = task2Start.plus(task2.getDuration());
        return !(task1End.isBefore(task2Start) || task2End.isBefore(task1Start));
    }

    private boolean isTaskTimeOverlapping(Task newTask) {
        return getPrioritizedTasks()
                .stream()
                .anyMatch(existingTask -> isOverlapping(newTask, existingTask));
    }

}