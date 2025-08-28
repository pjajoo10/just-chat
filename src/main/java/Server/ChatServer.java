package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ChatServer {

    static Map<String, Integer> idToUserId = new HashMap<>(Map.of(
            "clientA@gmail.com", 0,
            "clientB@gmail.com", 1,
            "clientC@gmail.com", 2,
            "clientD@gmail.com", 3,
            "clientE@gmail.com", 4));

    static Map<Integer, ClientSession> userIdToClientSession = new HashMap<>();

    public static class ClientSession{
        //Fields
        final Socket socket;
        String id;
        final DataOutputStream out;
        final DataInputStream in;

        ClientSession(Socket clientFacingSocket) throws IOException{
            socket= clientFacingSocket;
            out= new DataOutputStream(clientFacingSocket.getOutputStream());
            in=new DataInputStream(clientFacingSocket.getInputStream());
            id="authentication pending";
        }
    }

    public static void main(String[] args) throws IOException{
        //1. Listen for client connections
        //- Import a listening socket
        ServerSocket server = new ServerSocket();
        //- Configure the ip and port/Reserver the port
        String HOST = "0.0.0.0"; //Connection request sent to any interface of this server machine will be redirected to this socket because of this 0 IP
        int PORT=3000;
        server.bind(new InetSocketAddress(HOST, PORT), 6);
        System.out.println("Server is listening");
        
        //2. Accept and manage the connections
        ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
        while(true){

            Socket clientFacingSocket = server.accept();
            pool.submit(()->{
                try {
                    ClientSession client = new ClientSession(clientFacingSocket);
                    manageClient(client); 
                } catch (Exception e) {
                    // TODO: handle exception
                }
                
            });

        }
    }

    private static void manageClient(ClientSession client) throws IOException{

        System.out.println("Client connected");
        //1. Receive auth payload
        Map<String, Object> authPayload = readData(client.in);
        handlePayload(authPayload, client);

        //2. Listen for incomind data

        while(true){
            try {
                Map<String, Object> messagePayload = readData(client.in);
                handlePayload(messagePayload, client);
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
       

        //3. Process it depending on whether the recipient is online/offline
    }

    private static Map<String, Object> readData(DataInputStream in) throws IOException{

        int dataLength = in.readInt();
        byte[] buffer = new byte[dataLength];
        in.readFully(buffer);
        //Convert it to object (Deserialization (bytes->object) using Jackson)
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> payload = mapper.readValue(buffer, Map.class);

        return payload;

    }

    private static void sendData(Map<String,Object> payload,DataOutputStream out) throws IOException{
        //Serialise it to JSON string
        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = mapper.writeValueAsString(payload);
        //Serialise it to binary
        byte[] binaryPayload = jsonPayload.getBytes(StandardCharsets.UTF_8);
        //Calculate prefix length
        int payloadSize = binaryPayload.length;
        //Write the length first
        out.writeInt(payloadSize);
        //Write the data
        out.write(binaryPayload);
        out.flush();

    }

    private static void handlePayload(Map<String,Object> payload, ClientSession client) throws IOException{
        String payloadType = (String) payload.get("type");

        switch (payloadType) {
            case "auth":
                handleAuth(payload, client);
                break;

            case "message": handleMessage(payload,client);
                break;

            default:
                break;
        }

    }

    private static void handleAuth(Map<String, Object> payload, ClientSession client) throws IOException{

        Map<String,Object> data = (Map<String,Object>) payload.get("data");
        String clientId = (String) data.get("id");
        client.id = clientId;
        int clientUserId = idToUserId.get(clientId);
        userIdToClientSession.put(clientUserId, client);
        System.out.println("Authentication for " + clientId + " done");

    }

    private static void handleMessage(Map<String, Object> payload, ClientSession client) throws IOException{

        //Check if recipient is online, if yes relay, else try again later

        Map<String,Object> data = (Map<String,Object>) payload.get("data");
        String recipientId = (String) data.get("to");
        int recipientUserId = idToUserId.get(recipientId);
        ClientSession recipient = userIdToClientSession.get(recipientUserId);
        Map<String, Object> toBeSentPayload = new HashMap<>();
        Map<String, Object> toBeSentData = new HashMap<>();

        if(recipient.socket.isClosed()){
            //try again later
            System.out.println("Recipient is offline");
            toBeSentPayload.put("type", "ack");
            toBeSentData.put("status", 409);
            toBeSentPayload.put("data", toBeSentData);
            sendData(toBeSentPayload,client.out);
            System.out.println("ASked sender to try later");
            
        }

        else{
            // relay the message
            toBeSentPayload.put("type", "message");
            toBeSentData.put("from", client.id);
            toBeSentData.put("message", data.get("message"));
            toBeSentPayload.put("data", toBeSentData);
            sendData(toBeSentPayload,recipient.out);
            
        }


    }
}