import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.Random;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        // блок проверки создания объектов
        Task task1 = new Task("First task", "First task desc", Status.NEW);
        Task task2 = new Task("Second task", "Second task desc", Status.IN_PROGRESS);
        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic("First epic", "First epic desc");
        manager.createEpic(epic1);

        Subtask subtask1 = new Subtask("First subtask e1", "First subtask e1 desc", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Second subtask e1", "Second subtask e1 desc", Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("Third subtask e1", "Third subtask e2 desc", Status.NEW, epic1.getId());

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
        printAllTasksByManager(manager);

        //блок проверки изменения объектов
        Random randomID = new Random();
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getTaskById(2);
        manager.getTaskById(1);



        manager.getSubtaskById(4);
        manager.getSubtaskById(5);
        manager.getSubtaskById(5);
        manager.getSubtaskById(6);
        manager.getSubtaskById(6);
        manager.getSubtaskById(4);
        manager.getSubtaskById(5);
        manager.getSubtaskById(4);
        manager.getSubtaskById(6);
        manager.getSubtaskById(4);

        System.out.println("\n");
        printAllTasks(manager);

        manager.deleteEpicById(epic1.getId());


        System.out.println("\n\n");
        printAllTasks(manager);



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

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getAllSubtaskByEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

}
