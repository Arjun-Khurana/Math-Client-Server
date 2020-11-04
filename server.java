import java.net.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.nfunk.jep.*;
  
class server { 

  private static List<Connection> connections;

  public static void main(String args[]) throws Exception {

    DatagramSocket serverSocket = new DatagramSocket(9876);
    connections = new ArrayList<>();

    byte[] receiveData = new byte[1024]; 
    byte[] sendData  = new byte[1024]; 
    Instant first = null;

    while(true) 
    { 
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
      serverSocket.receive(receivePacket);

      Message message = new Message(new String(receivePacket.getData(), 0, receivePacket.getLength()));

      // Login: Add the client to the list
      if(message.getType().equals("login")) {
        System.out.println(message.getJSONString());
        first = Instant.now();
        Connection c = new Connection(message.getUser(), first);
        connections.add(c);
        System.out.println(connections.toString());

        // send ack back to client
      }
      
      // Logout
      else if(message.getType().equals("logout")) {
        Instant second = Instant.now();
        Connection current = null;
        for (Connection c : connections) {
          if (c.getUsername().equals(message.getUser())) {
            current = c;
            c.setEnd(second);
          }
        }
        
        String formatted = current.getFormattedTime();
        System.out.println(message.getJSONString() + formatted);
      }

      // Math request
      else {

        InetAddress IPAddress = receivePacket.getAddress();

        int port = receivePacket.getPort();

        System.out.println(message.getJSONString());

        String expression = message.getMessage();

        JEP jep = new JEP();
        jep.parseExpression(expression);
        Double result = jep.getValue();
        sendData = result.toString().getBytes();

        DatagramPacket sendPacket = new DatagramPacket(
          sendData,
          sendData.length,
          IPAddress,
          port
        );

        serverSocket.send(sendPacket);
      }

    }
  } 
}  
