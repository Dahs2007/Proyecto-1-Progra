package interpreter;

import opcodes.OpLiteral;
import opcodes.OpPushData;
import opcodes.Opcode;

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

    /**
     * Parse and execute a script (sequence of bytes) using a subset of Bitcoin Script
     * supported opcodes: push-data (1..75, PUSHDATA1/2), and OP_0..OP_16 literals.
     * Returns true if executed without failures.
     */
    public boolean run(byte[] script) {
        if (script == null) return false;
        int i = 0;
        while (i < script.length) {
            int op = script[i] & 0xff;
            i++;

            try {
                if (op == 0x00) { // OP_0
                    Opcode opcode = new OpLiteral(0);
                    opcode.execute(context);
                    continue;
                }

                if (op >= 0x01 && op <= 0x4b) { // direct push of op bytes
                    int len = op;
                    if (i + len > script.length) {
                        context.fail();
                        return false;
                    }
                    byte[] data = new byte[len];
                    System.arraycopy(script, i, data, 0, len);
                    i += len;
                    Opcode opcode = new OpPushData(data);
                    opcode.execute(context);
                    continue;
                }

                if (op == 0x4c) { // PUSHDATA1
                    if (i >= script.length) { context.fail(); return false; }
                    int len = script[i++] & 0xff;
                    if (i + len > script.length) { context.fail(); return false; }
                    byte[] data = new byte[len];
                    System.arraycopy(script, i, data, 0, len);
                    i += len;
                    new OpPushData(data).execute(context);
                    continue;
                }

                if (op == 0x4d) { // PUSHDATA2 (little-endian)
                    if (i + 1 >= script.length) { context.fail(); return false; }
                    int len = (script[i] & 0xff) | ((script[i+1] & 0xff) << 8);
                    i += 2;
                    if (i + len > script.length) { context.fail(); return false; }
                    byte[] data = new byte[len];
                    System.arraycopy(script, i, data, 0, len);
                    i += len;
                    new OpPushData(data).execute(context);
                    continue;
                }

                if (op >= 0x51 && op <= 0x60) { // OP_1 .. OP_16
                    int value = op - 0x50; // OP_1 == 0x51 -> 1
                    new OpLiteral(value).execute(context);
                    continue;
                }

                // Unsupported opcode: fail the script
                context.fail();
                return false;
            } catch (IllegalArgumentException e) {
                context.fail();
                return false;
            }
        }

        return !context.hasFailed();
    }
}
