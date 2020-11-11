import java.io.*; 
import java.net.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;
  
class client {

  private static InetAddress IPAddress;

  public static void sendMessage(Message message, DatagramSocket socket) throws IOException {
    byte[] sendData = message.getJSONString().getBytes();
    socket.send(new DatagramPacket(sendData, sendData.length, IPAddress, 9876));
  }

  public static void main(String args[]) throws Exception {
    
    Scanner s = new Scanner(System.in);

    System.out.println("Welcome to Math Client Server");
    
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    DatagramSocket clientSocket = new DatagramSocket();
    boolean ackReceived = false;

    IPAddress = InetAddress.getByName("127.0.0.1");

    byte[] receiveData = new byte[1024];

    // Prompt user for a username and login
    System.out.print("Enter a username: ");
    String username = inFromUser.readLine();
    Message loginMessage = new Message("login", username);
    sendMessage(loginMessage, clientSocket);

    DatagramPacket ack = new DatagramPacket(receiveData, receiveData.length); 
  
    clientSocket.receive(ack); 

    String ackResponse = new String(ack.getData()); 

    System.out.println("FROM SERVER:" + ackResponse);
    receiveData = new byte[1024];

    //wait for ack

    int x = -1;
    boolean userInput = false;

    while (x != 1 && x != 2) {
      System.out.println("Enter 1 for user input mode, or 2 for automatic mode");
      x = s.nextInt();
      System.out.println(x);
  
      if (x == 1) {
        userInput = true;
      } else if (x == 2) {
        userInput = false;
      } else {
        System.out.println("Invalid choice.");
      }
    }

    for (int i = 0; i < 3; i++) {
      String sentence;
      if (userInput) {
        System.out.print("Enter an expression: ");
        sentence = inFromUser.readLine(); 
        if (sentence.length() == 0) {
          System.out.print("Would you like to log out? (y/n): ");
          String answer = inFromUser.readLine();
          if(answer.equals("y")) {
            Message message = new Message("logout", username);
            sendMessage(message, clientSocket);
            s.close();
            return;
            // stop socket and thread
          }
          else {
            Message message = new Message("message", username, sentence);
            sendMessage(message, clientSocket);
          }
        }
      } else {
        sentence = generateExpression();
      }

      //Send message to server
      Message message = new Message("math request", username, sentence);
      sendMessage(message, clientSocket);

      // Receive response from server
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
    
      clientSocket.receive(receivePacket); 
  
      String response = new String(receivePacket.getData()); 
  
      System.out.println("FROM SERVER:" + response); 
      receiveData = new byte[1024];
      Thread.sleep(3000);
    }

    
    // Math request
    
    clientSocket.close(); 
    s.close();

  }

  // private static Message userExpression(String username) {

  // }

  private static String generateExpression() {
    Random r = new Random();
    String exp = "E+E";
    int depth = 0;
    while (exp.contains("E")) {
      if (++depth == 5) {
        exp = exp.replaceAll("E", "I");
        break;
      }
      int x = r.nextInt(6);

      switch(x) {
        case 0:
            exp = exp.replaceFirst("(?:E)+", "I");
            break;
        case 1:
            exp = exp.replaceFirst("(?:E)+", "(E+E)");
            break;
        case 2:
            exp = exp.replaceFirst("(?:E)+", "(E*E)");
            break;
        case 3:
            exp = exp.replaceFirst("(?:E)+", "(E-E)");
            break;
        case 4:
            exp = exp.replaceFirst("(?:E)+", "(E/E)");
            break;
        case 5:
            exp = exp.replaceFirst("(?:E)+", "(E^E)");
      }
    }

    while(exp.contains("I")) {
      exp = exp.replaceFirst("(?:I)+", Integer.toString(1 + r.nextInt(9)));
    }

    return exp;
  }

}
