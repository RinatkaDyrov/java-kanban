public class Main {
    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        // блок проверки создания объектов
        Task task1 = new Task("First task", "First task desc", Status.NEW);
        Task task2 = new Task("Second task", "Second task desc", Status.IN_PROGRESS);
        final int taskId1 = manager.createTask(task1);
        final int taskId2 = manager.createTask(task2);

        Epic epic1 = new Epic("First epic", "First epic desc");
        Epic epic2 = new Epic("Second epic", "Second epic desc");
        final int epicId1 = manager.createEpic(epic1);
        final int epicId2 = manager.createEpic(epic2);

        Subtask subtask1 = new Subtask("First subtask e1", "First subtask e1 desc", Status.NEW, epicId1);
        Subtask subtask2 = new Subtask("Second subtask e1", "Second subtask e1 desc", Status.NEW, epicId1);
        Subtask subtask3 = new Subtask("First subtask e2", "First subtask e2 desc", Status.NEW, epicId2);
        Subtask subtask4 = new Subtask("Second subtask e2", "First subtask e2 desc", Status.NEW, epicId2);

        int subtaskId1 = manager.createSubtask(subtask1);
        int subtaskId2 = manager.createSubtask(subtask2);
        int subtaskId3 = manager.createSubtask(subtask3);
        int subtaskId4 = manager.createSubtask(subtask4);

        printAllTasksByManager(manager);

        //блок проверки изменения объектов
        Subtask updSubtask2 = (Subtask) manager.getTaskById(subtaskId2);
        updSubtask2.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(updSubtask2);
        Subtask updSubtask3 = (Subtask) manager.getTaskById(subtaskId3);
        updSubtask3.setStatus(Status.DONE);
        manager.updateSubtask(updSubtask3);
        Subtask updSubtask4 = (Subtask) manager.getTaskById(subtaskId4);
        updSubtask4.setStatus(Status.DONE);
        manager.updateSubtask(updSubtask4);
        Task updTask = (Task) manager.getTaskById(taskId1);
        updTask.setStatus(Status.DONE);
        manager.updateTask(updTask);
        Epic updEpic = (Epic) manager.getTaskById(epicId1);
        updEpic.setStatus(Status.DONE);     //проверка неизменяемости статуса эпика
        updEpic.setName("First epic upd");
        manager.updateEpic(updEpic);

        printAllTasksByManager(manager);

        //блок проверки удаления объектов
        manager.deleteTaskById(taskId2);
        manager.deleteEpicById(epicId1);
        /*
        это не оговорено в ТЗ, но я решил, что удаление эпика удаляет все его подзадачи
        иначе они будут висеть неприкаянные
        */

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
