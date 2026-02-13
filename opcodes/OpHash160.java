package opcodes;

import interpreter.ExecutionContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OpHash160 implements Opcode {

    @Override
    public void execute(ExecutionContext context) {
        byte[] data = context.getStack().pop();
        byte[] hash = simulatedHash160(data);
        context.getStack().push(hash);
    }

    public static byte[] simulatedHash160(byte[] data) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] sha256Hash = sha256.digest(data);
            byte[] result = new byte[20];
            System.arraycopy(sha256Hash, 0, result, 0, 20);
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }
}
