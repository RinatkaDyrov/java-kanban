package manager;

import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
        if (file.isFile()) {
            loadFromFile(file);
        }
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,startTime,duration,epic");
            writer.newLine();

            for (Task task : getAllTasks()) {
                writer.write(toString(task));
                writer.newLine();
            }
            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic));
                writer.newLine();
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(toString(subtask));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                Task task = fromString(line);
                if (task != null) {
                    switch (task.getType()) {
                        case TASK -> createTask(task);
                        case EPIC -> createEpic((Epic) task);
                        case SUBTASK -> createSubtask((Subtask) task);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",");
        sb.append(task.getType()).append(",");
        sb.append(task.getName()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription()).append(",");
        if(task.getType() != TaskType.EPIC){
            sb.append(task.getStartTime() == null ? "" : task.getStartTime()).append(",");
            sb.append(task.getDuration().toMinutes()).append(",");
        }
        if (task.getType() == TaskType.SUBTASK) {
            sb.append(((Subtask) task).getEpicId());
        }
        return sb.toString();
    }

    private Task fromString(String value) {
        String[] parts = value.split(",");
        String taskType = parts[1];
        String name = parts[2];
        String status = parts[3];
        String description = parts[4];
        LocalDateTime startTime = parts[5].isEmpty() ? null : LocalDateTime.parse(parts[5]);
        Duration duration = parts[6].isEmpty() ? Duration.ZERO : Duration.ofMinutes(Long.parseLong(parts[6]));
        switch (taskType) {
            case "TASK" -> {
                Task task = new Task(name, description, Status.valueOf(status));
                task.setStartTime(startTime);
                task.setDuration(duration);
                return task;
            }
            case "EPIC" -> {
                return new Epic(name, description);
            }
            case "SUBTASK" -> {
                int epicId = Integer.parseInt(parts[7]);
                Subtask subtask = new Subtask(name, description, Status.valueOf(status), epicId);
                subtask.setStartTime(startTime);
                subtask.setDuration(duration);
                return subtask;
            }
        }
        return null;
    }


    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public ArrayList<Subtask> getAllSubtaskByEpic(int epicId) {
        return super.getAllSubtaskByEpic(epicId);
    }
}
