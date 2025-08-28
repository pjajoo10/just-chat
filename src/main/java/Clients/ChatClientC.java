package Clients;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatClientC {

    public static void main(String[] args) throws IOException {

        // Connect to the server
        Socket socket = new Socket();
       
        String HOST = "127.0.0.1";
        int PORT = 3000;

        socket.connect(new InetSocketAddress(HOST, PORT));
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Send auth payload
        Map<String, Object> authPayload = new HashMap<>();
        Map<String, Object> authData = new HashMap<>();
        authPayload.put("type", "auth");
        authData.put("id", "clientC@gmail.com");
        authPayload.put("data", authData);

        sendData(authPayload, new DataOutputStream(socket.getOutputStream()));

        // Listen for incoming messages

        ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();

        pool.submit(() -> {

            try {

                while (true) {
                    Map<String, Object> payload = readData(in);
                    handlePayload(payload);
                }

            } catch (Exception e) {
                // TODO: handle exception
            }

        });

        // Writing messages loop

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Write the receivers ID followed by a space and then the message");
            String recipient = sc.next();
            String text = sc.nextLine();

            Map<String, Object> messagePayload = new HashMap<>();
            Map<String, Object> messageData = new HashMap<>();
            messagePayload.put("type", "message");
            messageData.put("to", recipient);
            messageData.put("message", text);
            messagePayload.put("data", messageData);

            sendData(messagePayload, out);

        }

    }

    private static void sendData(Map<String, Object> payload, DataOutputStream out) throws IOException {
        // Serialise it to JSON string
        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = mapper.writeValueAsString(payload);
        // Serialise it to binary
        byte[] binaryPayload = jsonPayload.getBytes(StandardCharsets.UTF_8);
        // Calculate prefix length
        int payloadSize = binaryPayload.length;
        // Write the length first
        out.writeInt(payloadSize);
        // Write the data
        out.write(binaryPayload);
        out.flush();

    }

    private static Map<String, Object> readData(DataInputStream in) throws IOException {

        int dataLength = in.readInt();
        byte[] buffer = new byte[dataLength];
        in.readFully(buffer);
        // Convert it to object (Deserialization (bytes->object) using Jackson)
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> payload = mapper.readValue(buffer, Map.class);

        return payload;

    }

    private static void handlePayload(Map<String, Object> payload) throws IOException {
        String payloadType = (String) payload.get("type");

        switch (payloadType) {
            case "ack":
                handleAck(payload);
                break;

            case "message":
                handleMessage(payload);
                break;

            default:
                break;
        }

    }

    private static void handleAck(Map<String, Object> payload) throws IOException {

        System.out.println("Recipient is offline, try again later");

    }

    private static void handleMessage(Map<String, Object> payload) throws IOException {

        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        System.out.println("From: " + (String) data.get("from"));
        System.out.println("Message: " + (String) data.get("message"));
        System.out.println("\n");

    }

}