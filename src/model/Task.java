package model;

import java.util.Objects;

public class Task {

    private int id;
    private String name;
    private String description;
    private Status status;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Сравнение по ссылке
        if (o == null || getClass() != o.getClass()) return false; // Проверка на совпадение классов
        Task task = (Task) o;
        return id == task.id; // Уникальность задачи определяется только по id
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Хеш-код задачи основан только на id
    }

    @Override
    public String toString() {
        return STR."model.Task{name='\{name}\{'\''}, description='\{description}\{'\''}, status='\{getStatus()}\{'\''}\{'}'}";
    }
}
