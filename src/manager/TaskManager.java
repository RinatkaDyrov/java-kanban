package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private int taskID = 1;
    private final static HashMap<Integer, Task> taskList = new HashMap<>();
    private final static HashMap<Integer, Epic> epicList = new HashMap<>();
    private final static HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    public ArrayList<Task> getAllTasks(){
        return new ArrayList<>(taskList.values());
    }

    public ArrayList<Subtask> getAllSubtasks(){
        return new ArrayList<>(subtaskList.values());
    }

    public ArrayList<Epic> getAllEpics(){
        return new ArrayList<>(epicList.values());
    }

    public void deleteAllTasks(){
        taskList.clear();
    }

    public void deleteAllSubtasks(){
        subtaskList.clear();
        epicList.values().forEach(Epic::deleteSubtasks);
    }

    public void deleteAllSubtasksByEpic(Epic epic){
        epic.deleteSubtasks();
    }


    public void deleteAllEpics(){
        epicList.values().forEach(Epic::deleteSubtasks);
        epicList.clear();
        deleteAllSubtasks();
    }

    public Task getTaskById(int id){
        return taskList.get(id);
    }

    public Epic getEpicById(int id){
        return epicList.get(id);
    }

    public Subtask getSubtaskById(int id){
        return subtaskList.get(id);
    }

    public void createTask(Task task){
        taskList.put(taskID, task);
        task.setId(taskID);
        taskID++;
        /*возражу, так как айдишник же инициализирован со значением 1.
        можно инициализировать нулем и инкрементировать до добавления, но разницы же никакой
        */
    }

    public void createSubtask(Subtask subtask){
        subtaskList.put(taskID, subtask);
        epicList.get(subtask.getEpicId()).addSubtask(subtask);
        subtask.setId(taskID);
        taskID++;
    }

    public void createEpic(Epic epic){
        epicList.put(taskID, epic);
        epic.setId(taskID);
        taskID++;
    }

    public void updateTask(Task task){
        taskList.put(task.getId(), task);
    }

    public void updateEpic(Epic epic){
        epicList.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask){
        subtaskList.put(subtask.getId(), subtask);
        epicList.get(subtask.getEpicId()).getStatus();
        /*Я не уверен, что понял необходимость обновления статуса. Статус тасков и наследников приватный.
        * Получить статус мы можем через геттер только. Геттер расписан так, что сам считывает статус исходя из
        * статусов подзадач. И даже если мы сеттером зададим другой статус, геттер выдаст верное значение*/
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
        epicList.get(subtaskList.get(id).getEpicId()).getStatus();
    }

    public ArrayList<Subtask> getAllSubtaskByEpic(int epicId){
        return epicList.get(epicId).getSubtasks();
    }
}