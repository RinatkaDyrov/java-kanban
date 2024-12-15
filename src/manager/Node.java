package manager;

public class Node<T> {
    final T data; // Данные узла
    Node<T> prev; // Ссылка на предыдущий узел
    Node<T> next; // Ссылка на следующий узел

    public Node(T data, Node<T> prev, Node<T> next) {
        this.data = data;
        this.prev = prev;
        this.next = next;
    }
}
