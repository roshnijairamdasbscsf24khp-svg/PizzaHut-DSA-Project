package pizzahutproject;

public class UndoStack {
    // A simple Node class for the stack
    private class Node {
        String action;
        Node next;
        Node(String a) { action = a; next = null; }
    }

    private Node top;

    // Push an action onto the stack
    public void push(String action) {
        Node node = new Node(action);
        node.next = top;
        top = node;
    }

    // Pop the last action off the stack
    public String pop() {
        if (top == null) {
            return null;
        }
        String action = top.action;
        top = top.next;
        return action;
    }

    // Check if stack is empty
    public boolean isEmpty() {
        return top == null;
    }
}