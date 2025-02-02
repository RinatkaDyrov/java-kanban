package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TaskManager {

    ArrayList<Task> getAllTasks();

    ArrayList<Subtask> getAllSubtasks();

    ArrayList<Epic> getAllEpics();

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    Optional<Task> getTaskById(int id);

    Optional<Epic> getEpicById(int id);

    Optional<Subtask> getSubtaskById(int id);

    void createTask(Task task);

    void createSubtask(Subtask subtask);

    void createEpic(Epic epic);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    ArrayList<Subtask> getAllSubtaskByEpic(int id);

    List<Task> getHistory();

    Set<Task> getPrioritizedTasks();

    boolean isJsonIdValid(int jsonId);
}
