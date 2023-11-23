import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;


public class Server {



    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    public static void main(String[] args) throws Exception {

        int port = 12345; // Puerto de escucha

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Servidor en espera en el puerto " + port);

        Socket socket = serverSocket.accept();
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        System.out.println("Cliente conectado desde " + socket.getInetAddress());


        KeyPair serverKeyPair = generateKeyPair();
        PublicKey clientPublicKey = receivePublicKeyFromClient(socket);
        // Calculate shared secret
        sendPublicKeyToClient(socket, serverKeyPair.getPublic());
        SecretKey sharedSecret = calculateSharedSecret(serverKeyPair, clientPublicKey);

        //receivingFile(socket);

        System.out.println("Clave Server: "+serverKeyPair+" Clave cliente: "+clientPublicKey+" Calve compartida: "+sharedSecret);

        receiveFile(socket);

        dataInputStream.close();
        dataOutputStream.close();
        socket.close();
        serverSocket.close();
    }

    private static void receiveFile(Socket socket) throws Exception {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        String fileName = (String) objectInputStream.readObject();

        int bytes = 0;
        FileOutputStream fileOutputStream
                = new FileOutputStream(fileName);

        long size
                = dataInputStream.readLong(); // read file size
        byte[] buffer = new byte[4 * 1024];
        while (size > 0
                && (bytes = dataInputStream.read(
                buffer, 0,
                (int)Math.min(buffer.length, size)))
                != -1) {
            // Here we write the file using write method
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes; // read upto file size
        }
        // Here we received file
        System.out.println("File is Received");
        fileOutputStream.close();


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

