package opcodes;

import interpreter.ExecutionContext;

public interface Opcode {

    void execute(ExecutionContext context);

    default String getName() {
        return this.getClass().getSimpleName();
    }
}
