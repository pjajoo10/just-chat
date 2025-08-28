# One-on-One Simulated Chat System (v1)

## Overview
This is a one-on-one simulated chat system between 2 clients only.  
It is built as the first step toward developing a full chat platform.  
To keep things simple, we broke the full system down to its most essential component:  
the exchange of information between two clients.  

By starting here, we establish the foundation of a chat system while moving beyond planning and into actual implementation.

---

## Features
- Exchange information between two clients

---

## Architecture

### Scope
- Supports 2 clients only  
- No chat persistence  
- No store-and-forward mechanism (both users must be online to exchange messages)

### Components
- Server  
  - Relays information from one client to another  
- Clients  
  - Send and receive messages to/from another client  

### Interactions
1. Both clients connect to the server over TCP.  
2. Client A sends a message → Server receives it → forwards it to Client B.  
3. Client B responds with its own message → Server relays it back to Client A.  
4. This back-and-forth continues until the simulation ends.  

---

## Implementation (for listed features)
- Connection Protocol: TCP sockets (reliable delivery)  
- Concurrency Model: Server handles two client sessions simultaneously  
- Session Lifecycle: Ends when the simulation completes  

---

## Limitations (v1)
- No user input (pre-defined/simulated messages only)  
- No chat history (persistence)  
- No offline delivery (store-and-forward)  

---

## Next Steps (for v2)
- Make it interactive:
    - Replace simulation with real user input  
    -  Allow users to exit chat at will  

