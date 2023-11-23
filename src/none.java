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


}
