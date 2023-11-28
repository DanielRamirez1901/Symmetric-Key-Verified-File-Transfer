import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.Base64;



public class Server {
    private static final String status_OK = "The File is Correctly Sent";
    private static final String status_NO_OK = "The File isn't Correctly Sent";
    private static final int prime_number = 13;
    private static final int generator_of_p = 6;
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;



    //***********************************************MAIN****************************************************
    public static void main(String[] args) throws Exception {
        //Esperando conexion con cliente
        int port = 12345;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server waiting in the port " + port);

        //Conexion establecida
        Socket socket = serverSocket.accept();
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());
        System.out.println("Client connected in" + socket.getInetAddress());

        //Generacion Diffie-Hellman
        System.out.println("Diffie-Hellman");
        int privateServerKey = generatePrivateKey();
        int publicServerKey = generatePublicKey(privateServerKey);
        int clientPublicKey = receivePublicKeyFromClient(socket);
        sendPublicKeyToClient(socket, publicServerKey);
        int sharedSecret = calculateSharedSecret(privateServerKey, clientPublicKey);
        System.out.println("Llave privada server: "+privateServerKey+" Llave publica server: "+publicServerKey+" Llave publica del cliente: "+clientPublicKey+ " Llave compartida: "+sharedSecret );

        //Recibiendo HASH y Desencriptando File
        String hashClient = receiveHash(socket);
        File file = receiveEncryptedFile(socket, sharedSecret);

        //Calculando HASH nuevo file
        String fileHash = calculateFileHash(file);
        System.out.println("Hash of the decrypted file: " + fileHash);
        System.out.println("Hash of the original file: "+hashClient);

        //Comparacion entre codigos HASH
        Boolean fileStatus = hashComparison(fileHash,hashClient);

        socket.close();
        serverSocket.close();

        //Nueva conexion para enviar el file status
        int portReconnected = 12346;
        ServerSocket serverSocketReconnect = new ServerSocket(portReconnected);
        System.out.println("Server waiting in the port " + portReconnected);
        //Conexion establecida
        Socket socketReconnected = serverSocketReconnect.accept();
        //Envio de hash
        sendFileStatusToClient(fileStatus,socketReconnected);

        socketReconnected.close();
        serverSocketReconnect.close();
    }
    //*******************************************************************************************************



    //*********************************************DIFFIE-HELLMAN*********************************************
    private static Integer receivePublicKeyFromClient(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(dataInputStream);
        return (Integer) objectInputStream.readObject();
    }
    private static void sendPublicKeyToClient(Socket socket, Integer publicKey) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(dataOutputStream);
        objectOutputStream.writeObject(publicKey);
        objectOutputStream.flush();
    }
    private static int generatePublicKey(int privateKey) {
        BigInteger base = BigInteger.valueOf(generator_of_p);
        BigInteger exponent = BigInteger.valueOf(privateKey);
        BigInteger modulus = BigInteger.valueOf(prime_number);
        return base.modPow(exponent, modulus).intValue();
    }
    private static Integer generatePrivateKey(){
        return  (int)(Math.random()*5000+1);
    }
    private static int calculateSharedSecret(int privateKey, int publicKey) {
        BigInteger base = BigInteger.valueOf(publicKey);
        BigInteger exponent = BigInteger.valueOf(privateKey);
        BigInteger modulus = BigInteger.valueOf(prime_number);
        return base.modPow(exponent, modulus).intValue();
    }
    //*******************************************************************************************************



    //*********************************************HASH FILE*********************************************
    private static String calculateFileHash(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }

        fileInputStream.close();

        byte[] hashBytes = digest.digest();

        return Base64.getEncoder().encodeToString(hashBytes);
    }
    private static Boolean hashComparison(String fileHash, String hashClient){
        return fileHash.equals(hashClient);
    }
    private static String receiveHash(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(dataInputStream);
        return (String) objectInputStream.readObject();
    }
    //***************************************************************************************************



    //*********************************************DECRYPTED FILE*********************************************
    private static File receiveEncryptedFile(Socket socket, int sharedSecret) throws Exception {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        String fileName = (String) objectInputStream.readObject();

        try {
            //Array de bytes que representan la sharedSecret
            byte[] keyBytes = ByteBuffer.allocate(Integer.BYTES).putInt(sharedSecret).array();

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            keyBytes = sha.digest(keyBytes);

            SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);


            File decryptedFile = getFile(socket, cipher, fileName);

            return decryptedFile;
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // rethrow the exception after logging
        }
    }
    private static File getFile(Socket socket, Cipher cipher, String fileName) throws IOException {
        CipherInputStream cipherInputStream = new CipherInputStream(socket.getInputStream(), cipher);

        File decryptedFile = new File("Archivos/Salida/decrypted_" + fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(decryptedFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;

            // Read chunks of data from the CipherInputStream
            while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        }
        return decryptedFile;
    }
    //********************************************************************************************************



    //*********************************************FILE STATUS************************************************
    private static void sendFileStatusToClient(Boolean fileStatus,Socket socket) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        if(fileStatus) objectOutputStream.writeObject(status_OK);else objectOutputStream.writeObject(status_NO_OK);
        objectOutputStream.flush();
    }
    //********************************************************************************************************


}

