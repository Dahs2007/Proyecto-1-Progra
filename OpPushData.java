package opcodes;

import interpreter.ExecutionContext;
import java.util.Arrays;

public class OpPushData implements Opcode {

    private byte[] data;

    public OpPushData(byte[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    @Override
    public void execute(ExecutionContext context) {
        context.getStack().push(
            Arrays.copyOf(data, data.length)
        );
    }
}


