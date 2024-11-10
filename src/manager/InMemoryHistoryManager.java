package manager;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{

    ArrayList<Task> history = new ArrayList<>(10);

    @Override
    public void add(Task task) {
        if (task == null){
            return;
        }
        if (history.size() >= 10) {
            history.removeFirst();
        }
        Task copiedTask = new Task(task.getName(), task.getDescription(), task.getStatus());
        copiedTask.setId(task.getId());
        history.add(copiedTask);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
