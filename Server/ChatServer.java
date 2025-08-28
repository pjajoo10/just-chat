package Server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private static Socket clientA;
    private static Socket clientB;

    public static void main(String[] args) throws IOException, InterruptedException {

        // A. Listens for incoming client connection requests on a TCP port

        final String HOST = "0.0.0.0";
        final int PORT = 3000;

        // A.1 Import the socket handler
        ServerSocket server = new ServerSocket();

        // A.2 Bind the socket to ip,port
        server.bind(new InetSocketAddress(HOST, PORT), 2);

        System.out.println("Listening on: " + HOST + ":" + PORT);

        // A.3 Accept request, hand it off to a new thread for management. Repeat

        ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();

        clientA = server.accept();
        clientB = server.accept();

        pool.submit(() -> {
            try {
                manageClient(clientA);

            } catch (Exception e) {
                System.out.println(e);
            }

        });

        System.out.println("Client A is being managed");

        pool.submit(() -> {
            try {
                manageClient(clientB);

            } catch (Exception e) {
                System.out.println(e);
            }

        });

        System.out.println("Client B is being managed");

        Thread.sleep(Long.MAX_VALUE);

        // while (true) {

        // Socket client = server.accept();
        // if (clientA == null) {
        // clientA = client;
        // } else if (clientB == null) {
        // clientB = client;
        // }

        // pool.submit(() -> {
        // try {
        // manageClient(client);

        // } catch (Exception e) {
        // System.out.println(e);
        // }

        // });
        // }

    }

    private static void manageClient(Socket client) throws IOException {

        // 1. Read message

        Socket peer = (client == clientA) ? clientB : clientA;

        // InputStreamReader reader = new InputStreamReader(client.getInputStream(),
        // StandardCharsets.UTF_8);
        // OutputStreamWriter writer = new OutputStreamWriter(peer.getOutputStream(),
        // StandardCharsets.UTF_8);

        byte[] buffer = new byte[3000];
        int bytesRead;
        while ((bytesRead = client.getInputStream().read(buffer)) != -1) {
            peer.getOutputStream().write(buffer, 0, bytesRead);
        }

    }

}