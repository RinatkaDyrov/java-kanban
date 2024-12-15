package model;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    TaskManager manager;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
        epic = new Epic("Epic 1", "Description of Epic 1");
        manager.createEpic(epic);
        subtask1 = new Subtask("Subtask 1", "Description subtask 1", Status.NEW, epic.getId());
        subtask2 = new Subtask("Subtask 2", "Description subtask 2", Status.NEW, epic.getId());
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
    }

    @Test
    void shouldBeEqualsByArraysOfSubtasks() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);

        assertArrayEquals(subtasks.toArray(), manager.getEpicById(epic.getId()).getSubtasks().toArray());
    }

    @Test
    void shouldReturnTrueByComparingTwoIdenticalEpics() {
        Epic epic1 = epic;
        assertEquals(epic, epic1);
    }

    @Test
    void shouldReturnTrueAndExceptionOfClassTypesWhenSubtaskIsEpic() {
        Task epicThatFakedSubtask = new Epic("Epic 1", "Description of Epic 1");
        try {
            manager.createSubtask((Subtask) epicThatFakedSubtask);
            fail();
        } catch (ClassCastException exception) {
            assertTrue(true);
        }
    }

    @Test
    void shouldReturnNewStatusForEpicIfAllSubtasksStatusIsNew() {
        assertEquals(Status.NEW, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void shouldReturnDoneStatusForEpicIfAllSubtasksStatusIsDone() {
        manager.getAllSubtaskByEpic(manager.getEpicById(epic.getId()).getId()).forEach(subtask -> subtask.setStatus(Status.DONE));
        assertEquals(Status.DONE, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void shouldReturnInProgressStatusForEpicIfAnySubtaskHaveDifferentStatus() {
        Subtask subtask3 = new Subtask("Subtask 3", "Description subtask 3", Status.DONE, epic.getId());
        manager.createSubtask(subtask3);
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus());
    }

}