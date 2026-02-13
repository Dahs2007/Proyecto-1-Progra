package opcodes;

import interpreter.ExecutionContext;
import java.util.Arrays;

public class OpEqual implements Opcode {

    @Override
    public void execute(ExecutionContext context) {
        byte[] a = context.getStack().pop();
        byte[] b = context.getStack().pop();
        boolean equal = Arrays.equals(a, b);
        context.getStack().push(new byte[]{(byte) (equal ? 1 : 0)});
    }
}
