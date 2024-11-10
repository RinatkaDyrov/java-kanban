package model;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    TaskManager manager;
    Task task1;
    Task task2;

    @BeforeEach
    void setUp(){
        manager = Managers.getDefault();
        task1 = new Task("Task 1", "Task 1 description", Status.NEW);
        manager.createTask(task1);
        task2 = task1;
        manager.createTask(task2);
    }

    @Test
    void shouldReturnTrueByComparingOfTwoIdenticalTasks(){
        assertEquals(manager.getTaskById(task1.getId()), manager.getTaskById(task2.getId()));
    }
}