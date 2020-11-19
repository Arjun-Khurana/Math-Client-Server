package myapp.src.main.java.mavenpackage;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

/**
 * A client class to send math requests
 */
class client {

  private static InetAddress IPAddress;

  /**
   * Facilitates sending a Message object to the server
   *
   * @param message
   * @param socket
   * @throws java.io.IOException if any.
   */
  public static void sendMessage(Message message, DatagramSocket socket) throws IOException {
    byte[] sendData = message.getJSONString().getBytes();
    socket.send(new DatagramPacket(sendData, sendData.length, IPAddress, 9876));
  }

  /**
   * <p>main.</p>
   *
   * @param args
   * @throws java.lang.Exception if any.
   */
  public static void main(String args[]) throws Exception {

    Scanner s = new Scanner(System.in);

    System.out.println("Welcome to Math Client Server");

    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    DatagramSocket clientSocket = new DatagramSocket();

    IPAddress = InetAddress.getByName("127.0.0.1");

    byte[] receiveData = new byte[1024];

    // The client gives name during initial attachment to the server
    System.out.print("Enter a username: ");
    String username = inFromUser.readLine();
    Message loginMessage = new Message("login", username);
    sendMessage(loginMessage, clientSocket);

    // Client waits until it gets acknowledgement from the server for a successful connection.
    DatagramPacket ack = new DatagramPacket(receiveData, receiveData.length);

    clientSocket.receive(ack);
    Message ackMessage = new Message(new String(ack.getData(), 0, ack.getLength()));

    // String ackResponse = new String(ack.getData());
    if (ackMessage.getType().equals("ack")) {
      System.out.println("FROM SERVER: " + ackMessage.getMessage());
    }

    receiveData = new byte[1024];

    int x = -1;
    boolean userInput = false;

    // User input mode allows users to send their own math requests three times
    // Automatic mode will generate three random math requests and send them to the server
    while (x != 1 && x != 2) {
      System.out.println("Enter 1 for user input mode, or 2 for automatic mode");
      x = s.nextInt();
      // System.out.println(x);

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
        // The client sends a close connection request to the server and the application terminates.
        if (sentence.length() == 0) {
          System.out.print("Would you like to log out? (y/n): ");
          String answer = inFromUser.readLine();
          if (answer.equals("y")) {
            Message message = new Message("logout", username);
            sendMessage(message, clientSocket);
            s.close();
            return;
            // stop socket and thread
          } 
        }
      } else {
        sentence = generateExpression();
        System.out.println(sentence);
      }

      // Send message to server
      Message message = new Message("math request", username, sentence);
      sendMessage(message, clientSocket);

      // Receive response from server
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

      clientSocket.receive(receivePacket);

      String response = new String(receivePacket.getData());

      System.out.println("FROM SERVER: " + response);
      receiveData = new byte[1024];
      Random r = new Random();
      Thread.sleep((1 + r.nextInt(6))*1000);
    }

    Message message = new Message("logout", username);
    sendMessage(message, clientSocket);
    clientSocket.close();
    s.close();
    System.out.println("Thank you! Exiting.");
  }


  // Generates random math expressions
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

      switch (x) {
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

    while (exp.contains("I")) {
      exp = exp.replaceFirst("(?:I)+", Integer.toString(1 + r.nextInt(9)));
    }

    return exp;
  }

}
