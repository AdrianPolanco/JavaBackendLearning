package org.multithreading;

public class StandardStack<T> {
    private StackNode<T> head;
    private int count = 0;

    public synchronized void push(T value) {
        StackNode<T> newNode = new StackNode<>(value);
        newNode.next = head;
        head = newNode;
        count++;
    }

    public synchronized T pop() {
        if (head == null) {
            count++;
            return null;
        }
        T value = head.value;
        head = head.next;
        count++;
        return value;
    }

    public synchronized int getCount() {
        return count;
    }
}
