package interpreter;


import opcodes.OpLiteral;
import opcodes.OpPushData;
import opcodes.Opcode;
import opcodes.*;

public class ScriptInterpreter {

    private ExecutionContext context;
    private boolean traceEnabled;

    public ScriptInterpreter(ExecutionContext context) {
        this.context = context;
        this.traceEnabled = false;
    }

    public ExecutionContext getContext() {
        return context;
    }

    public void reset() {
        context.reset();
    }

    public void setTraceEnabled(boolean enabled) {
        this.traceEnabled = enabled;
    }

    public boolean validateResult() {
        if (context.hasFailed()) return false;
        if (context.getStack().isEmpty()) return false;
        byte[] top = context.getStack().peek();
        return !isZero(top);
    }

    private boolean isZero(byte[] value) {
        if (value == null || value.length == 0) return true;
        for (byte b : value) {
            if (b != 0) return false;
        }
        return true;
    }

    private void trace(String opName) {
        if (traceEnabled) {
            System.out.println("[TRACE] " + opName + " -> Pila: " + context.getStack().toString());
        }
    }

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

                if (op == 0x75) { // OP_DROP
                    new OpDrop().execute(context);
                    trace("OP_DROP");
                    continue;
                }

                if (op == 0x76) { // OP_DUP
                    new OpDup().execute(context);
                    trace("OP_DUP");
                    continue;
                }

                if (op == 0x87) { // OP_EQUAL
                    new OpEqual().execute(context);
                    trace("OP_EQUAL");
                    continue;
                }

                if (op == 0x88) { // OP_EQUALVERIFY
                    new OpEqualVerify().execute(context);
                    trace("OP_EQUALVERIFY");
                    if (context.hasFailed()) return false;
                    continue;
                }

                if (op == 0xa9) { // OP_HASH160
                    new OpHash160().execute(context);
                    trace("OP_HASH160");
                    continue;
                }

                if (op == 0xac) { // OP_CHECKSIG
                    new OpCheckSig().execute(context);
                    trace("OP_CHECKSIG");
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
