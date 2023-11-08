
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) throws Exception {

        int port = 12345; // Puerto de escucha

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Servidor en espera en el puerto " + port);

        Socket socket = serverSocket.accept();
        System.out.println("Cliente conectado desde " + socket.getInetAddress());

        // Implementar Diffie-Hellman para la negociación de claves
        // Implementar AES para la transferencia de archivos
        // Implementar SHA-256 para verificar la integridad del archivo

        socket.close();
        serverSocket.close();
    }
}
