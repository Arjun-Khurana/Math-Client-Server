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

    //Scanner for reading user input
    Scanner s = new Scanner(System.in);

    //radnom number generator for sleeping the thread
    Random r = new Random();

    System.out.println("Welcome to Math Client Server");

    //create datagramsocket for incoming messages
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    DatagramSocket clientSocket = new DatagramSocket();

    //ip address is localhost
    IPAddress = InetAddress.getByName("127.0.0.1");

    //byte array buffer for incoming data
    byte[] receiveData = new byte[1024];

    // The client gives name during initial attachment to the server
    System.out.print("Enter a username: ");
    String username = s.nextLine();
    Message loginMessage = new Message("login", username);
    sendMessage(loginMessage, clientSocket);

    // Client waits until it gets acknowledgement from the server for a successful connection.
    DatagramPacket ack = new DatagramPacket(receiveData, receiveData.length);

    clientSocket.receive(ack);
    Message ackMessage = new Message(new String(ack.getData(), 0, ack.getLength()));

    // String ackResponse = new String(ack.getData());
    if (ackMessage.getType().equals("ack")) {
      System.out.println("FROM SERVER: " + ackMessage.getMessage());
    } else {
      System.out.println("ERROR");
      s.close();
      clientSocket.close();
      return;
    }

    //redeclaring the array to clear the buffer
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

    //Create 3 math expressions in either automatic mode or user input mode
    for (int i = 0; i < 3; i++) {
      String sentence;
      if (userInput) {
        System.out.print("Enter an expression: ");
        sentence = inFromUser.readLine();
        // If the client just presses enter, the application asks for confirmation of logout
        // The client sends a close connection request to the server and the application terminates.
        if (sentence.length() == 0) {
          System.out.print("Would you like to log out? Press (y) to confirm: ");
          String answer = inFromUser.readLine();
          if (answer.equals("y")) {
            Message message = new Message("logout", username);
            sendMessage(message, clientSocket);
            s.close();
            clientSocket.close();
            System.out.println("Thank you! Exiting.");
            return;
            // stop socket and thread
          } else {
            i--;
            continue;
          }
        }
      } else {
        sentence = generateExpression();
        System.out.println(sentence);
      }

      // Send message to server with the math string
      Message message = new Message("math request", username, sentence);
      sendMessage(message, clientSocket);

      // Receive response from server
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

      clientSocket.receive(receivePacket);

      String response = new String(receivePacket.getData());

      //Print out the server response
      System.out.println("FROM SERVER: " + response);

      //Clear the buffer
      receiveData = new byte[1024];

      //Wait for 1-6 seconds before sending a new message
      Thread.sleep((1 + r.nextInt(6))*1000);
    }

    //After 3 math expressions, send a logout message and terminate
    Message message = new Message("logout", username);
    sendMessage(message, clientSocket);
    clientSocket.close();
    s.close();
    System.out.println("Thank you! Exiting.");
  }


  // Generates random math expressions
  private static String generateExpression() {
    //Random number generator 
    Random r = new Random();

    //Start with a basic addition of two expressions
    String exp = "E+E";
    int depth = 0;

    //Go while all expressions have not been replaced with integers
    while (exp.contains("E")) {
      //or if the depth is greater than 5 for simplicity
      if (++depth == 5) {
        exp = exp.replaceAll("E", "I");
        break;
      }

      //Switch on a random number to decide what to replace
      int x = r.nextInt(6);

      //replace the first instance of an expression with a new type of expression or an integer
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

    //Replace all expressions with a random integer
    while (exp.contains("I")) {
      exp = exp.replaceFirst("(?:I)+", Integer.toString(1 + r.nextInt(9)));
    }

    return exp;
  }

}
