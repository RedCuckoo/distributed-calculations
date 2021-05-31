import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketServer {
    protected int serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;

    private final DAOTask7 repository = new DAOTask7();
    private PrintWriter out;
    private BufferedReader in;

    public SocketServer(int port) {
        this.serverPort = port;
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SocketServer socket = new SocketServer(8080);
        socket.run();
    }

    public void run() {
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }

            String msg = null;
            try {
                msg = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Received message " + msg);

            executeDao(parseMessage(msg));
        }
        System.out.println("Server Stopped.");
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    private void executeDao(ArrayList<String> args) {
        switch (args.get(0)) {
            case "1" -> out.println(repository.getAllTypesByProductId(Integer.parseInt(args.get(1))));
        }
        out.flush();
    }

    private ArrayList<String> parseMessage(String str) {
        ArrayList<String> list = new ArrayList<>();
        String[] array = str.split(";");
        list.add(array[0]);
        for (int i = 1; i < array.length; i++) {
            String[] split = array[i].split("=");
            list.add(split[1]);
        }
        return list;
    }
}


