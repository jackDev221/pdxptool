package org.example.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConcurrentStack<T> {
    private List<T> stack;

    public ConcurrentStack() {
        stack = Collections.synchronizedList(new ArrayList<T>());
    }

    public void push(T item) {
        stack.add(item);
    }

    public T pop() {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.remove(0);
    }

    public int size() {
        return stack.size();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
