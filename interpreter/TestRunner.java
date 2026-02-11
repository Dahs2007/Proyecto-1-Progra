package interpreter;

public class TestRunner {

    public static void main(String[] args) {
        ExecutionContext ctx = new ExecutionContext();
        ScriptInterpreter interp = new ScriptInterpreter(ctx);

        // Build a script: push "hello" (5 bytes) then OP_1
        byte[] hello = new byte[] { 'h','e','l','l','o' };
        byte[] script = new byte[1 + hello.length + 1];
        int p = 0;
        script[p++] = (byte) hello.length; // push 5
        System.arraycopy(hello, 0, script, p, hello.length);
        p += hello.length;
        script[p++] = (byte) 0x51; // OP_1

        boolean ok = interp.run(script);
        System.out.println("Executed: " + ok);

        // Print stack contents (top first)
        System.out.println("Stack size: " + ctx.getStack().size());
        while (!ctx.getStack().isEmpty()) {
            byte[] v = ctx.getStack().pop();
            System.out.println(bytesToHex(v) + "  |  '" + new String(v) + "'");
        }
    }

    private static String bytesToHex(byte[] bytes) {
        if (bytes == null) return "null";
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
