package model;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

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
    void shouldAdd2SubtaskToEpic() {
        Epic epicById = manager.getEpicById(epic.getId()).isPresent() ? manager.getEpicById(epic.getId()).get() : null;
        assertNotNull(epicById);
        assertEquals(2, epicById.getSubtasks().size());
    }

    @Test
    void shouldReturnTrueAndExceptionOfClassTypesWhenEpicIsSubtask() {
        Task subtaskThatFakedEpic = new Subtask("Faked subtask", "Description of faked subtask", Status.NEW, epic.getId());
        try {
            manager.createEpic((Epic) subtaskThatFakedEpic);
            fail();
        } catch (ClassCastException exception) {
            assertTrue(true);
        }
    }

    @Test
    void shouldReturnTrueByComparingTwoIdenticalSubtasks() {
        Subtask subtaskComp = subtask1;
        assertEquals(subtaskComp, subtask1);
    }
}