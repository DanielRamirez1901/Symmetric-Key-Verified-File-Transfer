import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class Server {
    public static void main(String[] args) throws Exception {

        int port = 12345; // Puerto de escucha

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Servidor en espera en el puerto " + port);

        Socket socket = serverSocket.accept();
        System.out.println("Cliente conectado desde " + socket.getInetAddress());


        KeyPair serverKeyPair = generateKeyPair();
        PublicKey clientPublicKey = receivePublicKeyFromClient(socket);
        // Calculate shared secret
        sendPublicKeyToClient(socket, serverKeyPair.getPublic());
        SecretKey sharedSecret = calculateSharedSecret(serverKeyPair, clientPublicKey);

        //receivingFile(socket);

        System.out.println("Clave Server: "+serverKeyPair+" Clave cliente: "+clientPublicKey+" Calve compartida: "+sharedSecret);
        socket.close();
        serverSocket.close();
    }

    private static void receivingFile(Socket socket) throws IOException {
        // Crea un FileOutputStream para escribir el contenido del archivo recibido
        FileOutputStream fos = new FileOutputStream("archivo_recibido"); // Puedes cambiar el nombre según tu preferencia

        // Lee el contenido del archivo desde el InputStream del socket y escribe en FileOutputStream
        InputStream is = socket.getInputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = is.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }

        fos.close();

        System.out.println("Archivo recibido "+fos);
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(512); // You can adjust the key size as needed
        return keyPairGenerator.generateKeyPair();
    }

    private static PublicKey receivePublicKeyFromClient(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        return (PublicKey) objectInputStream.readObject();
    }

    private static SecretKey calculateSharedSecret(KeyPair serverKeyPair, PublicKey clientPublicKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(serverKeyPair.getPrivate());
        keyAgreement.doPhase(clientPublicKey, true);
        byte[] sharedSecretBytes = keyAgreement.generateSecret();

        // You may need to derive a key from the shared secret based on your requirements
        return new SecretKeySpec(sharedSecretBytes, 0, 16, "AES"); // Adjust as needed
    }

    private static void sendPublicKeyToClient(Socket socket, PublicKey publicKey) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(publicKey);
        objectOutputStream.flush();
    }

}


