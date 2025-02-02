package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    private int id;
    private TaskType type;
    private String name;
    private String description;
    private Status status;
    private Duration duration = Duration.ZERO;
    private LocalDateTime startTime = null;

    public Task() {
        this.type = TaskType.TASK;
    }

    public Task(String name, String description, Status status) {
        this.type = TaskType.TASK;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description) {
        this.type = TaskType.TASK;
        this.name = name;
        this.description = description;
    }

    public LocalDateTime getEndTime() {
        return startTime != null && duration != null ? startTime.plus(duration) : null;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String startTime = getStartTime() == null ? " " : getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
        String duration = getDuration() == Duration.ZERO ? " " : String.valueOf(getDuration().toMinutes());

        return String.format("model.Task{name='%s', description='%s', status='%s', startTime='%s', duration='%s'}",
                name, description, getStatus(), startTime, duration);
    }
}
