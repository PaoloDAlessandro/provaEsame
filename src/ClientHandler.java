import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.*;

public class ClientHandler implements Runnable {

    Socket clientSocket;
    BufferedReader in;
    PrintWriter out;

    ClientHandler (Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run () {
        //this.inizializeClientHandler();
        /*
        try {
            this.executeClientHandler();
        } catch (SocketException e) {
            System.out.println("error");
        }
        */
        this.collectData();
    }

    void inizializeClientHandler () {
        try {
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("reader failed" + e);
        }

        out = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void executeClientHandler() throws SocketException {
        String s = "";
        while (true) {
            try {
                s = receive();
                System.out.println(s);
                out.println(s.toUpperCase());
                if (s == "") break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Client: " + clientSocket.getLocalAddress() + " disconnected from the server");
    }


    String receive() {
        String s = "";
        try {
            s = in.readLine();
        } catch (IOException e) {
            tryReconnect();
        }
        return s;
    }

    private void tryReconnect() {
        try {
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void collectData() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3006/socialyze",
                    "root",
                    "");
        } catch (Exception e) {
            System.out.println(e);
            System.exit(-1);
        }

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from users");
            while (rs.next())
                System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3));
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
