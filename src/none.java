public class none {


        /*
        Server
    private static void DHKeyPairProcess(Socket socket) throws Exception {
        KeyPair serverKeyPair = SecurityManager.generateDHKeyPair();
        // Recibir clave pública del cliente
        PublicKey clientPublicKey = receivePublicKey(socket);

        // Enviar clave pública al cliente
        sendPublicKey(socket, serverKeyPair.getPublic());
        // Generar clave compartida
        SecretKey sharedSecretKey = SecurityManager.generateSharedSecretKey(serverKeyPair.getPrivate(), clientPublicKey);

        System.out.println("Clave compartida generada: " + SecurityManager.encodeKey(sharedSecretKey.getEncoded()));

    }

    private static PublicKey receivePublicKey(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        return (PublicKey) objectInputStream.readObject();
    }

    private static void sendPublicKey(Socket socket, PublicKey publicKey) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(publicKey);
    }

     */

        /*

        Client
    private static void DHKeyPairProcess(Socket socket) throws Exception {
        KeyPair serverKeyPair = SecurityManager.generateDHKeyPair();

        // Enviar clave pública al servidor
        sendPublicKey(socket, serverKeyPair.getPublic());

        // Recibir clave pública del servidor
        PublicKey clientPublicKey = receivePublicKey(socket);


        // Generar clave compartida
        SecretKey sharedSecretKey = SecurityManager.generateSharedSecretKey(serverKeyPair.getPrivate(), clientPublicKey);

        System.out.println("Clave compartida generada: " + SecurityManager.encodeKey(sharedSecretKey.getEncoded()));

    }

    private static PublicKey receivePublicKey(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        return (PublicKey) objectInputStream.readObject();
    }

    private static void sendPublicKey(Socket socket, PublicKey publicKey) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(publicKey);
    }

     */
 /*
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
     */
    /*
    private static void receiveEncryptedFile(Socket socket, SecretKey sharedSecret) throws Exception {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        String fileName = (String) objectInputStream.readObject();

        // Initialize AES Cipher with shared key
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sharedSecret);

        // Wrap the input stream with CipherInputStream for decryption
        CipherInputStream cipherInputStream = new CipherInputStream(dataInputStream, cipher);

        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        int bytes;
        while ((bytes = cipherInputStream.read()) != -1) {
            fileOutputStream.write(bytes);
        }

        cipherInputStream.close();
        fileOutputStream.close();
    }

     */

        /*
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
     */

}
