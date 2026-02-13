package stack;

import java.util.ArrayDeque;
import java.util.Deque;

public class ScriptStack {

    private Deque<byte[]> stack;
    private int maxSize;

    public ScriptStack() {
        stack = new ArrayDeque<>();
        maxSize = 1000;
    }

    public void push(byte[] value) {
        if (stack.size() >= maxSize) {
            throw new RuntimeException("Stack overflow");
        }
        stack.push(value);
    }

    public byte[] pop() {
        if (stack.isEmpty()) {
            throw new RuntimeException("Stack empty");
        }
        return stack.pop();
    }

    public byte[] peek() {
        if (stack.isEmpty()) {
            throw new RuntimeException("Stack empty");
        }
        return stack.peek();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int size() {
        return stack.size();
    }

    public void clear() {
        stack.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (byte[] elem : stack) {
            if (!first) sb.append(", ");
            first = false;
            sb.append(bytesToHex(elem));
        }
        sb.append("]");
        return sb.toString();
    }

    private static String bytesToHex(byte[] bytes) {
        if (bytes == null) return "null";
        if (bytes.length == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
