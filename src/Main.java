import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        // блок проверки создания объектов
        Task task1 = new Task("First task", "First task desc", Status.NEW);
        Task task2 = new Task("Second task", "Second task desc", Status.IN_PROGRESS);
        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic("First epic", "First epic desc");
        Epic epic2 = new Epic("Second epic", "Second epic desc");
        manager.createEpic(epic1);
        manager.createEpic(epic2);

        Subtask subtask1 = new Subtask("First subtask e1", "First subtask e1 desc", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Second subtask e1", "Second subtask e1 desc", Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("First subtask e2", "First subtask e2 desc", Status.NEW, epic2.getId());
        Subtask subtask4 = new Subtask("Second subtask e2", "First subtask e2 desc", Status.NEW, epic2.getId());

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
        manager.createSubtask(subtask4);

        printAllTasksByManager(manager);

        //блок проверки изменения объектов
        Subtask updSubtask2 = manager.getSubtaskById(subtask2.getId());
        updSubtask2.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(updSubtask2);
        Subtask updSubtask3 = manager.getSubtaskById(subtask3.getId());
        updSubtask3.setStatus(Status.DONE);
        manager.updateSubtask(updSubtask3);
        Subtask updSubtask4 = manager.getSubtaskById(subtask4.getId());
        updSubtask4.setStatus(Status.DONE);
        manager.updateSubtask(updSubtask4);
        Task updTask = manager.getTaskById(task1.getId());
        updTask.setStatus(Status.DONE);
        manager.updateTask(updTask);
        Epic updEpic = manager.getEpicById(epic1.getId());
        updEpic.setStatus(Status.DONE);     //проверка неизменяемости статуса эпика
        updEpic.setName("First epic upd");
        manager.updateEpic(updEpic);

        printAllTasksByManager(manager);

        //блок проверки удаления объектов
        manager.deleteTaskById(task2.getId());
        manager.deleteEpicById(epic1.getId());

        printAllTasksByManager(manager);
    }

    public static void printAllTasksByManager(TaskManager manager){
        System.out.println("Список задач:");
        manager.getAllTasks().forEach(System.out::println);
        System.out.println("Список эпиков:");
        manager.getAllEpics().forEach(System.out::println);
        System.out.println("Список подзадач по эпикам:");
        manager.getAllEpics().forEach(epic -> {
            System.out.println("Эпик " + epic.getId() + ": ");
            epic.getSubtasks().forEach(System.out::println);
        });
        System.out.println("==============================================");
    }
}
