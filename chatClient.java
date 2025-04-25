import java.io.*;
import java.net.*;

public class ChatClient {
   private static final String SERVER_ADDRESS = "localhost";
   private static final int PORT = 12345;
   private String name;

   public static void main(String[] args) {
      new ChatClient().start();
   }

   public void start() {
      try (Socket socket = new Socket(SERVER_ADDRESS, PORT)) {
         System.out.println("Connected to the chat server successfully.");

         BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter writer = new PrintWriter(socket.getOutputStream());
         BufferedReader userReader = new BufferedReader(new InputStreamReader(System.in));

         new Thread(() -> {
            try {
               String message;
               while ((message = reader.readLine()) != null) {
                  System.out.println(message);
               }
            } catch (IOException e) {
               System.out.println("Disconnected from chat server.");
            }
         }).start();

         // get name from user and send it to the server
         System.out.println(reader.readLine());
         name = userReader.rea

         // send messages to the server
         String message;
         while((message = userReader.readLine()) != null){
            if(message.equalsIgnoreCase("quit")){
               writer.println(message);
               break;
            }
            writer.println(message);
         }
      } catch (IOException e) {
         System.out.println("Error occured when connecting to the chat server: " + e.getMessage());
      }
   }
}
