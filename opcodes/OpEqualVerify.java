package opcodes;

import interpreter.ExecutionContext;
import java.util.Arrays;

public class OpEqualVerify implements Opcode {

    @Override
    public void execute(ExecutionContext context) {
        byte[] a = context.getStack().pop();
        byte[] b = context.getStack().pop();
        if (!Arrays.equals(a, b)) {
            context.fail();
        }
    }
}
