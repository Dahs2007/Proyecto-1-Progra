package opcodes;

import interpreter.ExecutionContext;

public class OpLiteral implements Opcode {

    private int value;

    public OpLiteral(int value) {
        if (value < 0 || value > 16) {
            throw new IllegalArgumentException("Literal out of range");
        }
        this.value = value;
    }

    @Override
    public void execute(ExecutionContext context) {
        context.getStack().push(new byte[]{(byte) value});
    }
}
