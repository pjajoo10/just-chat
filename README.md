# One-on-One Chat System – v2

## Overview  
This is the second version of the one-on-one chat system. In v1, message exchange between two clients was simulated. In v2, simulation has been replaced with live user input: users can type whatever they want in real time. By allowing manual input and free exit, v2 takes the system one step closer to a functioning chat platform.

## Features
- Live message exchange between two clients using user input (no simulation)  
- Users can type arbitrary messages  
- Users can exit the chat at any time by typing `exit`  

## Scope
- Still supports only 2 clients

## Architecture  

### Components  
- **Server**  
  - Relays messages between clients  
  - Handles connections and detects exit commands  

- **Clients**  
  - Connect to the server  
  - Send and receive messages in real time  
  - Allow users to exit the chat gracefully  

### Interactions  
- Each client connects to the server over TCP.  
- When a user types a message, the client sends it to the server, which forwards it to the other client.  
- When a user types `exit`, the server closes their connection.  

## Implementation Details  

- **Connection Protocol:** TCP  
- **Major Change from v1:** Removed automated simulation, added real-time input from users.  
- **Exit Handling:** Typing `exit` cleanly terminates a user’s session.  

## Next Steps (Planned for v3)  
- Support for multiple clients simultaneously
