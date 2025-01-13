package publicMethodsTests;

import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicClassTest {
    private Epic epic;

    @BeforeEach
    void setUp() {
        epic = new Epic("Epic", "Test epic");
    }

    @Test
    void shouldBeNewWhenAllSubtasksNew() {
        epic.addSubtask(new Subtask("Subtask 1", "Desc", Status.NEW, epic.getId()));
        epic.addSubtask(new Subtask("Subtask 2", "Desc", Status.NEW, epic.getId()));
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void shouldBeDoneWhenAllSubtasksDone() {
        epic.addSubtask(new Subtask("Subtask 1", "Desc", Status.DONE, epic.getId()));
        epic.addSubtask(new Subtask("Subtask 2", "Desc", Status.DONE, epic.getId()));
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void shouldBeInProgressWhenSubtasksNewAndDone() {
        epic.addSubtask(new Subtask("Subtask 1", "Desc", Status.NEW, epic.getId()));
        epic.addSubtask(new Subtask("Subtask 2", "Desc", Status.DONE, epic.getId()));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void shouldBeInProgressWhenAllSubtasksInProgress() {
        epic.addSubtask(new Subtask("Subtask 1", "Desc", Status.IN_PROGRESS, epic.getId()));
        epic.addSubtask(new Subtask("Subtask 2", "Desc", Status.IN_PROGRESS, epic.getId()));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}
