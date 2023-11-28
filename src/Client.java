import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class Client {
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;
    private static final int prime_number = 13;
    private static final int generator_of_p = 6;
    private static String fileName = "";
    private static String filePath = "";



    //*********************************************MAIN*********************************************
    public static void main(String[] args) throws Exception {
        //Cliente esperando conexion con server
        String serverAddress = "127.0.0.1";
        int port = 12345;
        System.out.println("Waiting Connection of server");

        //Cliente conectado al server
        Socket socket = new Socket(serverAddress, port);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());
        System.out.println("*************Connecting to the Server*************");

        //Menu para el cliente
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        fileMenu(reader);

        //Generacion Diffie-Hellman
        System.out.println("Diffie-Hellman");
        int clientPrivateKey = generatePrivateKey();
        int clientPublicKey = generatePublicKey(clientPrivateKey);
        sendPublicKeyToServer(clientPublicKey);
        int serverPublicKey = receivePublicKeyFromServer(socket);
        int sharedSecret = calculateSharedSecret(clientPrivateKey, serverPublicKey);

        System.out.println("Llave privada cliente: "+clientPrivateKey+" Llave publica cliente: "+clientPublicKey+" Llave publica del servidor: "+serverPublicKey+ " Llave compartida: "+sharedSecret );

        //Calculando Hash del archivo y enviandolo al Server
        System.out.println("Calculating HASH");
        String sha256Hash = calculateFileHash(filePath);
        sendHashToServer(sha256Hash, socket);

        //Enviando archivo cifrado al server
        System.out.println("Sending File to the server");
        sendEncryptedFile(fileName, filePath, socket, sharedSecret);

        //Conexion a nuevo socket para recibir el estado del archivo
        Thread.sleep(1000);
        int portReconnected = 12346;
        System.out.println("New connection created");
        Socket socketReconnected = new Socket(serverAddress, portReconnected);
        receiveFileStatus(socketReconnected);

        socket.close();
        socketReconnected.close();
    }
    //**********************************************************************************************



    //*********************************************MENU*********************************************
    private static void fileMenu(BufferedReader reader) throws IOException {

        System.out.println("*************Menu*************");
        System.out.println("1. Select one of the program files");
        System.out.println("2. Enter the file path manually");

        System.out.println("Enter your choice (1 or 2):");
        int option = Integer.parseInt(reader.readLine());

        switch (option) {
            case 1 -> {
                // Opción 1: Seleccionar uno de los archivos del programa
                System.out.println("*************Available files*************");
                System.out.println("1. logica proyecto.pdf");
                System.out.println("2. imagen.png");
                String[] archivosDisponibles = {"logica proyecto.pdf", "imagen.png"};
                int selection;
                do {
                    selection = Integer.parseInt(reader.readLine());

                    if (selection == 1) {
                        Client.fileName = archivosDisponibles[selection - 1];
                        filePath = "Archivos/Entrada/logica proyecto.pdf";
                    } else if (selection == 2) {
                        Client.fileName = archivosDisponibles[selection - 1];
                        filePath = "Archivos/Entrada/imagen.png";
                    } else {
                        System.out.println("Invalid selection. Please choose an option within the range.");
                    }
                } while (!(selection <= archivosDisponibles.length));
            }
            case 2 -> {
                String filePath;
                File file;
                do {
                    System.out.println("*************Enter the file path*************");
                    filePath = reader.readLine();
                    file = new File(filePath);
                    if (!file.exists() || file.isDirectory()) {
                        System.out.println("Invalid path. Please verify and try again.");
                    }
                } while (!file.exists() || file.isDirectory());

                //Obtengo nombre archivo en ruta
                String fileName = file.getName();
                //Obtengo la posicion en donde esta el . en el nombre
                int dotIndex = fileName.lastIndexOf('.');
                //Valido si es posicion correcta y si es asi obtengo su extension
                String fileExtension = fileName.substring(dotIndex + 1);


                System.out.println("Write the File Name:");
                fileName = reader.readLine();
                Client.fileName = fileName + "." + fileExtension;
                Client.filePath = filePath;
            }
            default -> System.out.println("Invalid option");
        }

    }
    //**********************************************************************************************



    //*********************************************DIFFIE-HELLMAN*********************************************
    private static Integer receivePublicKeyFromServer(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        return (Integer) objectInputStream.readObject();
    }
    private static void sendPublicKeyToServer(Integer publicKey) throws IOException {
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
    //**********************************************************************************************************



    //*********************************************HASH CODE*********************************************
    public static String calculateFileHash(String filePath) throws NoSuchAlgorithmException {
        try {
            Path path = Paths.get(filePath);
            byte[] fileBytes = Files.readAllBytes(path);
            //Creacion de instancia del algorirmo sha-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //Arreglo de bytes que representa el hash SHA-256
            byte[] hashBytes = digest.digest(fileBytes);

            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static void sendHashToServer(String hash, Socket socket) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(dataOutputStream);
        objectOutputStream.writeObject(hash);
        objectOutputStream.flush();
    }
    //***************************************************************************************************



    //*********************************************FILE ENCRYPTED*********************************************
    private static void sendEncryptedFile(String fileName, String filePath, Socket socket, int sharedSecret) throws Exception {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(fileName);
        objectOutputStream.flush();


        //Array de bytes que representan la sharedSecret
        byte[] keyBytes = ByteBuffer.allocate(Integer.BYTES).putInt(sharedSecret).array();


        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        keyBytes = sha.digest(keyBytes);

        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        System.out.println(secretKey.hashCode());

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);


        CipherOutputStream cipherOutputStream = getCipherOutputStream(filePath, socket, cipher);

        cipherOutputStream.close();

    }
    private static CipherOutputStream getCipherOutputStream(String filePath, Socket socket, Cipher cipher) throws IOException {
        CipherOutputStream cipherOutputStream = new CipherOutputStream(socket.getOutputStream(), cipher);

        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath))) {
            int bytesRead;
            byte[] buffer = new byte[8192];


            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, bytesRead);
            }

        }
        return cipherOutputStream;
    }
    //********************************************************************************************************



    //*********************************************FILE STATUS*********************************************
    private static void receiveFileStatus(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        String status = (String) objectInputStream.readObject();
        System.out.println("The file Status is: "+status);
    }
    //*****************************************************************************************************
}