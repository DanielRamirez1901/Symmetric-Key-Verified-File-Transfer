import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;

public class Client {

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));



        String serverAddress = "127.0.0.1"; // Cambiar a la dirección IP del servidor
        int port = 12345; // Puerto del servidor
        System.out.println("Esperando conexion con el servidor....");


        Socket socket = new Socket(serverAddress, port);
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        System.out.println("Conectado al servidor");


        System.out.println("Ingrese el nombre del archivo:");
        String fileName = reader.readLine();
        fileName = fileName+".pdf";
        String filePath = "C://Users//danir//Downloads//logica proyecto.pdf";


        KeyPair clientKeyPair = generateKeyPair();
        sendPublicKeyToServer(socket, clientKeyPair.getPublic());
        // Receive server's public key
        PublicKey serverPublicKey = receivePublicKeyFromServer(socket);
        // Calculate shared secret
        SecretKey sharedSecret = calculateSharedSecret(clientKeyPair, serverPublicKey);


        System.out.println("Sending the File to the Server");

        //Call SendFile Method
        sendFile(fileName,filePath,socket);
        String sha256Hash = calculateFileHash(filePath);
        sendHashToServer(sha256Hash, socket);


        System.out.println("Clave Client: "+clientKeyPair+" Clave Server: "+serverPublicKey+" Clave compartida: "+sharedSecret);

        socket.close();
        dataInputStream.close();
        dataInputStream.close();
    }

    private static void sendFile(String fileName,String filePath,Socket socket) throws Exception {

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(fileName);
        objectOutputStream.flush();

        int bytes = 0;
        // Open the File where he located in your pc
        File file = new File(filePath);
        FileInputStream fileInputStream
                = new FileInputStream(file);

        // Here we send the File to Server
        dataOutputStream.writeLong(file.length());
        // Here we  break file into chunks
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer))
                != -1) {
            // Send the file to Server Socket
            dataOutputStream.write(buffer, 0, bytes);
            dataOutputStream.flush();
        }
        // close the file here
        fileInputStream.close();
    }

    private static void sendHashToServer(String hash, Socket socket) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(hash);
        objectOutputStream.flush();
    }


    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(512); // You can adjust the key size as needed
        return keyPairGenerator.generateKeyPair();
    }

    private static void sendPublicKeyToServer(Socket socket, PublicKey publicKey) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(publicKey);
        objectOutputStream.flush();
    }

    private static PublicKey receivePublicKeyFromServer(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        return (PublicKey) objectInputStream.readObject();
    }

    private static SecretKey calculateSharedSecret(KeyPair clientKeyPair, PublicKey serverPublicKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(clientKeyPair.getPrivate());
        keyAgreement.doPhase(serverPublicKey, true);
        byte[] sharedSecretBytes = keyAgreement.generateSecret();

        // You may need to derive a key from the shared secret based on your requirements
        return new SecretKeySpec(sharedSecretBytes, 0, 16, "AES"); // Adjust as needed
    }


    public static String calculateFileHash(String filePath) throws NoSuchAlgorithmException {
        try {
            Path path = Paths.get(filePath);
            byte[] fileBytes = Files.readAllBytes(path);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(fileBytes);

            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}