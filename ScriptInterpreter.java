package interpreter;

public class ScriptInterpreter {

    private ExecutionContext context;

    public ScriptInterpreter(ExecutionContext context) {
        this.context = context;
    }

    public ExecutionContext getContext() {
        return context;
    }

    public void reset() {
        context.reset();
    }
}

