import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import com.google.gson.Gson;


public class ClientHandler implements Runnable {

    Socket clientSocket;
    BufferedReader in;
    PrintWriter out;
    static ArrayList<City> cities = new ArrayList<City>();

    ClientHandler (Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run () {
        this.buildCities();
        this.inizializeClientHandler();
        try {
            this.executeClientHandler();
        } catch (SocketException e) {
            System.out.println("error");
        }
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
        Gson gson = new Gson();
        String s;
        while (true) {
                s = receive();
                System.out.println(s);
                try {
                    switch (s) {
                        default:
                            out.println(s + " is not a command");
                            break;
                        case "hottest":
                            out.println(gson.toJson(searchMaxTemp()));
                            break;

                        case "all":
                            out.println(gson.toJson(cities));
                            break;

                        case "sorted_by_temp":
                            sort_by_temp();
                            out.println(gson.toJson(cities));
                            break;

                        case "sorted_by_name":
                            sort_by_name();
                            out.println(gson.toJson(cities));
                        }

                } catch (NullPointerException e) {
                    System.out.println("Client: " + clientSocket.getLocalAddress() + " disconnected from the server");
                    break;
                }

                if (s == "") break;
            }
    }


    String receive() {
        String s = "";
        try {
            s = in.readLine();
        } catch (IOException e) {
            //tryReconnect();
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

    public void buildCities() {
        cities.add(new City(3,"Toronto",15.9));
        cities.add(new City(33,"Milan",25.94));
        cities.add(new City(55,"Rome",35.4));
    }

    City searchMaxTemp() {
        sort_by_temp();
        return cities.get(0);
    }

    void sort_by_temp() {
        cities.sort(((o1, o2) -> {
            if (o1.getTemp() < o2.getTemp())
                return 1;
            if (o1.getTemp() > o2.getTemp())
                return -1;
            return 0;
        }));
    }

    void sort_by_name() {
        cities.sort((o1, o2) -> {
            return o1.getName().compareTo(o2.getName());
        });
    }

}