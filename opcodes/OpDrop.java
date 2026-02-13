package opcodes;

import interpreter.ExecutionContext;
import java.util.Arrays;

public class OpDup implements Opcode {

    @Override
    public void execute(ExecutionContext context) {
        byte[] top = context.getStack().peek();
        context.getStack().push(Arrays.copyOf(top, top.length));
    }
}
