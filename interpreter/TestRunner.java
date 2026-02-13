package interpreter;

import opcodes.OpHash160;

public class TestRunner {

    public static void main(String[] args) {
        System.out.println("=== Interprete Bitcoin Script - Pruebas Fase 1 ===\n");
        testP2PKH();
        testP2PKHIncorrecto();
        testOpEqual();
    }

    private static void testP2PKH() {
        System.out.println("--- Test P2PKH correcto ---");
        ExecutionContext ctx = new ExecutionContext();
        ScriptInterpreter interp = new ScriptInterpreter(ctx);
        interp.setTraceEnabled(true);

        byte[] signature = new byte[]{0x30, 0x45, 0x02, 0x21};
        byte[] pubKey = new byte[]{0x04, 0x11, 0x22, 0x33, 0x44};
        byte[] pubKeyHash = OpHash160.simulatedHash160(pubKey);

        int scriptLen = 1 + signature.length + 1 + pubKey.length + 1 + 1 + 1 + pubKeyHash.length + 1 + 1;
        byte[] script = new byte[scriptLen];
        int p = 0;

        script[p++] = (byte) signature.length;
        System.arraycopy(signature, 0, script, p, signature.length);
        p += signature.length;

        script[p++] = (byte) pubKey.length;
        System.arraycopy(pubKey, 0, script, p, pubKey.length);
        p += pubKey.length;

        script[p++] = (byte) 0x76; // OP_DUP
        script[p++] = (byte) 0xa9; // OP_HASH160
        script[p++] = (byte) pubKeyHash.length;
        System.arraycopy(pubKeyHash, 0, script, p, pubKeyHash.length);
        p += pubKeyHash.length;
        script[p++] = (byte) 0x88; // OP_EQUALVERIFY
        script[p++] = (byte) 0xac; // OP_CHECKSIG

        boolean ok = interp.run(script);
        boolean valid = interp.validateResult();
        System.out.println("  Ejecutado: " + ok + " | Valido: " + valid + " (esperado: true)\n");
    }

    private static void testP2PKHIncorrecto() {
        System.out.println("--- Test P2PKH incorrecto ---");
        ExecutionContext ctx = new ExecutionContext();
        ScriptInterpreter interp = new ScriptInterpreter(ctx);

        byte[] signature = new byte[]{0x30, 0x45, 0x02, 0x21};
        byte[] pubKey = new byte[]{0x04, 0x11, 0x22, 0x33, 0x44};
        byte[] wrongHash = new byte[20];
        wrongHash[0] = (byte) 0xFF;

        int scriptLen = 1 + signature.length + 1 + pubKey.length + 1 + 1 + 1 + wrongHash.length + 1 + 1;
        byte[] script = new byte[scriptLen];
        int p = 0;

        script[p++] = (byte) signature.length;
        System.arraycopy(signature, 0, script, p, signature.length);
        p += signature.length;
        script[p++] = (byte) pubKey.length;
        System.arraycopy(pubKey, 0, script, p, pubKey.length);
        p += pubKey.length;
        script[p++] = (byte) 0x76;
        script[p++] = (byte) 0xa9;
        script[p++] = (byte) wrongHash.length;
        System.arraycopy(wrongHash, 0, script, p, wrongHash.length);
        p += wrongHash.length;
        script[p++] = (byte) 0x88;
        script[p++] = (byte) 0xac;

        boolean ok = interp.run(script);
        boolean valid = interp.validateResult();
        System.out.println("  Ejecutado: " + ok + " | Valido: " + valid + " (esperado: false)\n");
    }

    private static void testOpEqual() {
        System.out.println("--- Test OP_EQUAL ---");
        ExecutionContext ctx = new ExecutionContext();
        ScriptInterpreter interp = new ScriptInterpreter(ctx);

        byte[] script = new byte[]{(byte) 0x55, (byte) 0x55, (byte) 0x87};
        boolean ok = interp.run(script);
        boolean valid = interp.validateResult();
        System.out.println("  OP_5 OP_5 OP_EQUAL -> Valido: " + valid + " (esperado: true)\n");
    }
}
