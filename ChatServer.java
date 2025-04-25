import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
   private static final int PORT = 12345;
   private static final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();
   private static final Map<String, ClientHandler> clientMap = new ConcurrentHashMap<>();

   public static void main(String[] args) {
      System.out.println("Server is listening on port " + PORT);
      try (
            ServerSocket serverSocket = new ServerSocket(PORT)) {
         while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
         }
      } catch (IOException e) {
         System.err.println("Server Error: " + e.getMessage());
      }
   }

   public static void broadcast(String message, ClientHandler sender) {
      for (ClientHandler client : clients) {
         if (client != sender) {
            client.sendMessage(message);
         }
      }
   }

   public static void addClient(String name, ClientHandler client) {
      if (clientMap.containsKey(name)) {
         client.sendMessage("Error: This name is already taken. Please choose a different name.");
         client.shutdown();
      } else {
         clientMap.put(name, client);
         broadcast(name + " has entered the chat!", client);
      }
   }

   public static void removeClient(String name, ClientHandler client) {
      clients.remove(client);
      clientMap.remove(name);
      broadcast(name + " has left the chat.", null);
   }

   private static class ClientHandler implements Runnable {
      private Socket socket;
      private PrintWriter writer;
      private BufferedReader reader;
      private String name;

      public ClientHandler(Socket socket) {
         this.socket = socket;
      }

      @Override
      public void run() {
         try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // read client name
            writer.println("Please enter your name: ");
            name = reader.readLine();
            addClient(name, this);

            writer.println("Welcome to the chat, " + name + "! Please type 'quit' if you wish to leave the chat.");

            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
               if (clientMessage.equalsIgnoreCase("quit")) {
                  break;
               }
               broadcast(name + ": " + clientMessage, this);
            }
         } catch (IOException e) {
            System.out.println("Error occurred with client " + name + ": " + e.getMessage());
         } finally {
            try {
               removeClient(name, this);
               socket.close();
            } catch (IOException e) {
               System.out.println("Error occurred while closing socket for client " + name);
            }
         }
      }

      public void sendMessage(String message) {
         writer.println(message);
      }

      public void shutdown() {
         try {
            socket.close();
         } catch (IOException e) {
            // do nothing
         }
      }

   }

}
