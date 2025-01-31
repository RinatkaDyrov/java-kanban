package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private final ArrayList<Subtask> subtasks = new ArrayList<>();
    private Duration duration = Duration.ZERO;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.setType(TaskType.EPIC);
    }

    public void addSubtask(Subtask subtask) {
        calculateTimeFields();
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void deleteAllSubtasks() {
        calculateTimeFields();
        subtasks.clear();
    }

    public void deleteSubtask(Subtask subtask) {
        calculateTimeFields();
        subtasks.remove(subtask);
    }

    @Override
    public Status getStatus() {
        if (subtasks.isEmpty()) {
            return Status.NEW;
        }
        long countDone = subtasks.stream().filter(subtask -> subtask.getStatus() == Status.DONE).count();
        long countNew = subtasks.stream().filter(subtask -> subtask.getStatus() == Status.NEW).count();
        if (countNew == subtasks.size()) {
            return Status.NEW;
        }
        return countDone == subtasks.size() ? Status.DONE : Status.IN_PROGRESS;
    }

    private void calculateTimeFields() {
        startTime = subtasks.stream().map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        endTime = subtasks.stream().map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        duration = startTime != null && endTime != null ? Duration.between(startTime, endTime) : Duration.ZERO;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}
