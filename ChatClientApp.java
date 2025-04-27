import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;

public class ChatClientApp extends Application {
   private TextArea chatArea;
   private TextField inputField;
   private PrintWriter out;
   private String name;

   public static void main(String[] args) {
      launch(args);
   }

   @Override
   public void start(Stage primaryStage) {

      chatArea = new TextArea();
      chatArea.setEditable(false);

      inputField = new TextField();
      Button sendButton = new Button("Send");

      VBox root = new VBox(10);
      root.setPadding(new Insets(10));

      ScrollPane scrollPane = new ScrollPane(chatArea);
      scrollPane.setFitToWidth(true);
      scrollPane.setFitToHeight(true);

      HBox inputBox = new HBox(5);
      inputBox.getChildren().addAll(inputField, sendButton);
      HBox.setHgrow(inputField, Priority.ALWAYS);

      root.getChildren().addAll(scrollPane, inputBox);
      VBox.setVgrow(scrollPane, Priority.ALWAYS);

      Scene scene = new Scene(root, 500, 400);
      primaryStage.setTitle("Chat Client");
      primaryStage.setScene(scene);
      primaryStage.show();

      // get name
      showUsernameDialog(primaryStage);

      sendButton.setOnAction(e -> sendMessage());
      inputField.setOnAction(e -> sendMessage());
   }

   private void showUsernameDialog(Stage owner) {
      TextInputDialog dialog = new TextInputDialog("Anonymous");
      dialog.setTitle("Username");
      dialog.setHeaderText("Enter your chat username");
      dialog.setContentText("Name:");

      dialog.initOwner(owner);
      dialog.showAndWait().ifPresent(result -> {
         name = result.trim().isEmpty() ? "Anonymous" : result;
         connectToServer();
      });
   }

   private void connectToServer() {
      try {
         Socket socket = new Socket("localhost", 12345);
         out = new PrintWriter(socket.getOutputStream(), true);

         // send name to the server
         out.println(name);

         // seperate thread for receiving messages from server
         new Thread(() -> {
            try (BufferedReader in = new BufferedReader(
                  new InputStreamReader(socket.getInputStream()))) {

               String serverMessage;
               while ((serverMessage = in.readLine()) != null) {
                  String finalMessage = serverMessage;
                  Platform.runLater(() -> chatArea.appendText(finalMessage + "\n"));
               }
            } catch (IOException e) {
               Platform.runLater(() -> chatArea.appendText("Disconnected from server\n"));
            }
         }).start();

      } catch (IOException e) {
         Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setTitle("Connection Error");
         alert.setHeaderText("Cannot connect to server");
         alert.setContentText(e.getMessage());
         alert.showAndWait();
      }
   }

   private void sendMessage() {
      String message = inputField.getText();
      if (!message.isEmpty()) {
         out.println(message);
         inputField.clear();
         Platform.runLater(() -> chatArea.appendText("You: " + message + "\n"));
         if (message.equalsIgnoreCase("quit")) {
            Platform.exit();
         }
      }
   }
}