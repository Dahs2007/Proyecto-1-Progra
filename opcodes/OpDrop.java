package opcodes;

import interpreter.ExecutionContext;

public class OpDrop implements Opcode {

    @Override
    public void execute(ExecutionContext context) {
        context.getStack().pop();
    }
}
