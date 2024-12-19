package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node<Task>> nodeMap = new HashMap<>();
    private final CustomLinkedList<Task> historyList = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        Node<Task> existingNode = nodeMap.get(task.getId());
        if (existingNode != null) {
            historyList.removeNode(existingNode);
        }

        Node<Task> newNode = historyList.linkLast(task);
        nodeMap.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        Node<Task> node = nodeMap.remove(id);
        if (node != null) {
            historyList.removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    private static class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;

        public Node<T> linkLast(T element) {
            Node<T> newNode = new Node<>(element, tail, null);
            if (tail != null) {
                tail.next = newNode;
            } else {
                head = newNode;
            }
            tail = newNode;
            return newNode;
        }

        public void removeNode(Node<T> node) {
            if (node == null) {
                return;
            }
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                head = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                tail = node.prev;
            }
        }

        public List<T> getTasks() {
            List<T> tasks = new ArrayList<>();
            Node<T> current = head;
            while (current != null) {
                tasks.add(current.data);
                current = current.next;
            }
            return tasks;
        }
    }
}