import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static int portNumer = 8000;

    static Socket clientSocket = null;

    public static void main(String[] args) {

        ServerSocket serverSocket = openToServer();
        System.out.println("Server socket started at port n: " + serverSocket.getLocalPort());
        while (true) {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("failed to accept client request connection" + e);
            }

            System.out.println(clientSocket.getLocalAddress());

            ClientHandler clientHandler = new ClientHandler(clientSocket);

            new Thread(clientHandler).start();
        }

    }

    static private ServerSocket openToServer() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(portNumer);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return serverSocket;
    }
}
