package model;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    @Override
    public Status getStatus() {
        int countDone = 0;
        int countNew = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus().equals(Status.NEW)) {
                countNew++;
            }
            if (subtask.getStatus().equals(Status.DONE)) {
                countDone++;
            }
        }
        if (countNew == subtasks.size() || subtasks.isEmpty()) return Status.NEW;
        return countDone == subtasks.size() ? Status.DONE : Status.IN_PROGRESS;
    }
}
