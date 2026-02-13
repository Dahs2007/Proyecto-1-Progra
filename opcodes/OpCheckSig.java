package opcodes;

import interpreter.ExecutionContext;

public class OpCheckSig implements Opcode {

    @Override
    public void execute(ExecutionContext context) {
        byte[] pubKey = context.getStack().pop();
        byte[] sig = context.getStack().pop();
        boolean valid = (sig.length > 0 && pubKey.length > 0);
        context.getStack().push(new byte[]{(byte) (valid ? 1 : 0)});
    }
}
