import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    static String hostName = "127.0.0.1";
    static int portNumber = 8000;
    static PrintWriter out = null;
    static BufferedReader in = null;
    static Socket echoSocket = null;
    static BufferedReader stgIn = null;


    public static void main(String[] args) {
        inizializeConnection();
        while (stgIn != null) {
            try {
                executeConnection(stgIn.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void inizializeConnection() {
        try {
            echoSocket = new Socket(hostName, portNumber);
        } catch (IOException e){
            System.out.println("Cannot reach server" + e);
        }

        try {
            out = new PrintWriter(echoSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

         stgIn = new BufferedReader(
                new InputStreamReader(System.in));
        try {
            in = new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void executeConnection (String send) {
        String received = "";
        out.println(send);
        System.out.println("sended " + send);

        try {
            received = in.readLine();
            System.out.println("received: " + received);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
