package pizzahutproject;

public class Menu {
    private static class Node {
        Pizza pizza;
        Node next;
        Node(Pizza p) { pizza = p; next = null; }
    }
    private Node head;
    private int size = 0;

    public void addFirst(Pizza p) {
        Node n = new Node(p);
        n.next = head;
        head = n;
        size++;
    }

    public void addLast(Pizza p) {
        Node n = new Node(p);
        if (head == null) { head = n; size++; return; }
        Node cur = head;
        while (cur.next != null) cur = cur.next;
        cur.next = n; size++;
    }

    // Critical for Admin Panel
    public Pizza removeAt(int idx) {
        if (idx < 0 || idx >= size) return null;
        if (idx == 0) {
            Pizza ret = head.pizza;
            head = head.next;
            size--;
            return ret;
        }
        Node cur = head;
        for (int i = 0; i < idx - 1; i++) cur = cur.next;
        if (cur.next == null) return null;

        Pizza ret = cur.next.pizza;
        cur.next = cur.next.next;
        size--;
        return ret;
    }

    public Pizza getAt(int idx) {
        if (idx < 0 || idx >= size) return null;
        Node cur = head;
        for (int i = 0; i < idx; i++) cur = cur.next;
        return cur.pizza;
    }

    public int size() { return size; }

    public void displayMenu() {
        Node cur = head; int i = 1;
        while (cur != null) {
            System.out.println(i + ". " + cur.pizza.detailedString());
            cur = cur.next; i++;
        }
    }

    public void displayMenuCompact() {
        Node cur = head; int i = 1;
        while (cur != null) {
            System.out.println(i + ". " + cur.pizza.name);
            cur = cur.next; i++;
        }
    }
}