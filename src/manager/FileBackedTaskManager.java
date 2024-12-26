package manager;

import model.*;

import java.io.*;
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
            writer.write("id,type,name,status,description,epic");
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
                    if (task instanceof Epic) {
                        createEpic((Epic) task);
                    } else if (task instanceof Subtask) {
                        createSubtask((Subtask) task);
                    } else {
                        createTask(task);
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
        if (task instanceof Epic) {
            sb.append(TaskType.EPIC).append(",");
        }
        if (task instanceof Subtask) {
            sb.append(TaskType.SUBTASK).append(",");
        } else {
            sb.append(TaskType.TASK).append(",");
        }
        sb.append(task.getName()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription()).append(",");
        if (task instanceof Subtask) {
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
        switch (taskType) {
            case "TASK" -> {
                return new Task(name, description, Status.valueOf(status));
            }
            case "EPIC" -> {
                return new Epic(name, description);
            }
            case "SUBTASK" -> {
                int epicId = Integer.parseInt(parts[5]);
                return new Subtask(name, description, Status.valueOf(status), epicId);
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
