import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {

    private static int taskID = 1;
    private final static HashMap<Integer, Task> taskList = new HashMap<>();
    private final static HashMap<Integer, Epic> epicList = new HashMap<>();
    private final static HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    public Collection<Task> getAllTasks(){
        return taskList.values();
    }

    public Collection<Subtask> getAllSubtasks(){
        return subtaskList.values();
    }

    public Collection<Epic> getAllEpics(){
        return epicList.values();
    }

    public void deleteAllTasks(){
        taskList.clear();
    }

    public void deleteAllSubtasks(){
        subtaskList.clear();
    }

    public void deleteAllSubtasksByEpic(Epic epic){
        epic.deleteSubtasks();
    }


    public void deleteAllEpics(){
        epicList.clear();
    }

    public Object getTaskById(int id){
        if (taskList.containsKey(id)) return taskList.get(id);
        if (epicList.containsKey(id)) return epicList.get(id);
        return subtaskList.get(id);
    }

    public int createTask(Task task){
        taskList.put(taskID, task);
        task.setId(taskID);
        taskID++;
        return task.getId();
    }

    public int createSubtask(Subtask subtask){
        subtaskList.put(taskID, subtask);
        epicList.get(subtask.getEpicId()).addSubtask(subtask);
        subtask.setId(taskID);
        taskID++;
        return subtask.getId();
    }

    public int createEpic(Epic epic){
        epicList.put(taskID, epic);
        epic.setId(taskID);
        taskID++;
        return epic.getId();
    }

    public void updateTask(Task task){
        taskList.put(task.getId(), task);
    }

    public void updateEpic(Epic epic){
        epicList.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask){
        subtaskList.put(subtask.getId(), subtask);
    }

    public void deleteTaskById(int id){
        taskList.remove(id);
    }

    public void deleteEpicById(int id){
        epicList.get(id).getSubtasks().forEach(subtask -> subtaskList.remove(subtask.getId()));
        epicList.remove(id);
    }

    public void deleteSubtaskById(int id){
        subtaskList.remove(id);
    }

    public ArrayList<Subtask> getAllSubtaskByEpic(Epic epic){
        return epic.getSubtasks();
    }
}