import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatClientB {

    public static void main(String[] args) throws IOException, InterruptedException {

        // A. Connects to the server

        final String HOST = "127.0.0.1";
        final int PORT = 3000;
        final int TIMEOUT_MS = 5000;

        // A.1 Create a TCP socket
        Socket socket = new Socket();

        // A.2 Configure it with server's ip and port / Connect with server
        socket.connect(new InetSocketAddress(HOST, PORT), TIMEOUT_MS); // timeout because its a blocking call

        // B. Send the payload

        // B.1 Create the writer
        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);

        // B.2 Write the text using Writer
        writer.write("Hi client A, My name is client B");

        writer.flush();

        // C. Listen for incoming payloads and read it using Reader

        ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();

        InputStreamReader reader = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);

        pool.submit(() -> {

            char[] buffer = new char[3000];
            int charactersRead;
            try {

                while ((charactersRead = reader.read(buffer)) != -1) {
                    String received = new String(buffer, 0, charactersRead);
                    System.out.print(received);
                }

            } catch (Exception e) {
                // TODO: handle exception
                System.out.println(e);
            }

        });

        // socket.close();

        Thread.sleep(Long.MAX_VALUE);

    }

}
