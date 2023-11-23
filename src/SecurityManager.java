import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import java.math.BigInteger;
import java.security.*;
import java.util.Base64;

public class SecurityManager {
    public static KeyPair generateDHKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(2048); // Tamaño de la clave, puedes ajustarlo según tus necesidades
        return keyPairGenerator.generateKeyPair();
    }

    public static SecretKey generateSharedSecretKey(PrivateKey privateKey, PublicKey publicKey) throws Exception {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(privateKey);
        keyAgreement.doPhase(publicKey, true);
        return keyAgreement.generateSecret("AES/ECB/PKCS5Padding");
    }
    
    public static byte[] getP() {
        // Debes reemplazar esto con una forma segura de obtener o generar el primo 'p'
        return new BigInteger("23").toByteArray();
    }

    public static byte[] getG() {
        // Debes reemplazar esto con una forma segura de obtener o generar el generador 'g'
        return new BigInteger("5").toByteArray();
    }

    public static String encodeKey(byte[] key) {
        return Base64.getEncoder().encodeToString(key);
    }
}
