import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class server {
   private static final int PORT = 12345;
   private static final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();
   private static final Map<String, ClientHandler> clientMap = new ConcurrentHashMap<>();

   public static void main(String[] args) {
      System.out.println("Server starting on port " + PORT);

      try (ServerSocket serverSocket = new ServerSocket(PORT)) {
         while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
         }
      } catch (IOException e) {
         System.err.println("Server exception: " + e.getMessage());
      }
   }

   public static void broadcast(String message, ClientHandler excludeClient) {
      for (ClientHandler client : clients) {
         if (client != excludeClient) {
            client.sendMessage(message);
         }
      }
   }

   public static void registerClient(String name, ClientHandler client) {
      if (clientMap.containsKey(name)) {
         client.sendMessage("ERROR: Name already taken. Please choose another name.");
         client.shutdown();
      } else {
         clientMap.put(name, client);
         broadcast(name + " has joined the chat!", client);
      }
   }

   public static void removeClient(String name, ClientHandler client) {
      clients.remove(client);
      clientMap.remove(name);
      broadcast(name + " has left the chat.", null);
   }

   private static class ClientHandler implements Runnable {
      private Socket socket;
      private PrintWriter out;
      private BufferedReader in;
      private String name;

      public ClientHandler(Socket socket) {
         this.socket = socket;
      }

      @Override
      public void run() {
         try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Get client name
            out.println("Please enter your name:");
            name = in.readLine();
            registerClient(name, this);

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
               if (clientMessage.equalsIgnoreCase("terminate")) {
                  break;
               }
               broadcast(name + ": " + clientMessage, this);
            }
         } catch (IOException e) {
            System.out.println("Error with client " + name + ": " + e.getMessage());
         } finally {
            try {
               removeClient(name, this);
               socket.close();
            } catch (IOException e) {
               System.out.println("Error closing socket for " + name);
            }
         }
      }

      public void sendMessage(String message) {
         out.println(message);
      }

      public void shutdown() {
         try {
            socket.close();
         } catch (IOException e) {
            // Ignore
         }
      }
   }
}