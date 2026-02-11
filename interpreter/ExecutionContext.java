package interpreter;

import stack.ScriptStack;

public class ExecutionContext {

    private ScriptStack stack;
    private boolean failed;

    public ExecutionContext() {
        stack = new ScriptStack();
        failed = false;
    }

    public ScriptStack getStack() {
        return stack;
    }

    public void fail() {
        failed = true;
    }

    public boolean hasFailed() {
        return failed;
    }

    public void reset() {
        stack.clear();
        failed = false;
    }
}
