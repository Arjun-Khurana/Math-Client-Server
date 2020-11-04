import java.io.*; 
import java.net.*;
import java.util.Random;
  
class client {

  private static InetAddress IPAddress;

  public static void sendMessage(Message message, DatagramSocket socket) throws IOException {
    byte[] sendData = message.getJSONString().getBytes();
    socket.send(new DatagramPacket(sendData, sendData.length, IPAddress, 9876));
  }

  public static void main(String args[]) throws Exception {

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

    //wait for ack

    String sentence = inFromUser.readLine(); 
    if (sentence.length() == 0) {
      System.out.print("Would you like to log out? (y/N): ");
      String answer = inFromUser.readLine();
      if(answer.equals("y")) {
        Message message = new Message("logout", username);
        sendMessage(message, clientSocket);
        return;
        // stop socket and thread
      }
      else {
        Message message = new Message("message", username, sentence);
        sendMessage(message, clientSocket);
      }
    }
    Message message = new Message("message", username, sentence);
    sendMessage(message, clientSocket);
    
    // Receive response from server
    DatagramPacket receivePacket = 
        new DatagramPacket(receiveData, receiveData.length); 

    clientSocket.receive(receivePacket); 

    String modifiedSentence = 
        new String(receivePacket.getData()); 

    System.out.println("FROM SERVER:" + modifiedSentence); 
    clientSocket.close(); 

  }

  private static String generateExpression() {
    Random r = new Random();
    String exp = "E";
    // exp = exp.replaceFirst("(?:E)+", "I");
    // System.out.println(exp);
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
