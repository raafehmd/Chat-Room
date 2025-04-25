import java.io.*;
import java.net.*;

public class client0 {
   private static final String SERVER_ADDRESS = "localhost";
   private static final int PORT = 12345;
   private String name;

   public static void main(String[] args) {
      new client0().start();
   }

   public void start() {
      try (Socket socket = new Socket(SERVER_ADDRESS, PORT)) {
         System.out.println("Connected to chat server");

         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

         // Handle server messages in a separate thread
         new Thread(() -> {
            try {
               String serverMessage;
               while ((serverMessage = in.readLine()) != null) {
                  System.out.println(serverMessage);
               }
            } catch (IOException e) {
               System.out.println("Disconnected from server");
            }
         }).start();

         // Get name from user and send to server
         System.out.print(in.readLine());
         name = userInput.readLine();
         out.println(name);

         // Send messages to server
         String message;
         while ((message = userInput.readLine()) != null) {
            if (message.equalsIgnoreCase("terminate")) {
               out.println(message);
               break;
            }
            out.println(message);
         }
      } catch (IOException e) {
         System.out.println("Error connecting to server: " + e.getMessage());
      }
   }
}