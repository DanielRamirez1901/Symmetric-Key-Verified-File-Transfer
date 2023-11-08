

import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {

        String serverAddress = "127.0.0.1"; // Cambiar a la dirección IP del servidor
        int port = 12345; // Puerto del servidor

        // Implementar Diffie-Hellman para la negociación de claves
        // Implementar AES para la transferencia de archivos
        // Implementar SHA-256 para calcular el hash del archivo

        Socket socket = new Socket(serverAddress, port);
        System.out.println("Conectado al servidor");

        // Implementar la transferencia del archivo

        socket.close();
    }
}
