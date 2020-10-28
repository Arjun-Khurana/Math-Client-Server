import java.net.*;
// import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.Duration;

  
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

      // Add the client to the list
      if(message.getType().equals("login")) {
        System.out.println(message.getJSONString());
        first = Instant.now();
        Connection c = new Connection(message.getUser(), first);
        connections.add(c);
        System.out.println(connections.toString());

        // send ack back to client
      }
      // Remove the client from the list
      else if(message.getType().equals("logout")) {
        Instant second = Instant.now();
        long difference = second.toEpochMilli() - first.toEpochMilli();
        Duration duration = new Duration(difference);
        PeriodFormatter formatter = PeriodFormat.getDefault();
        String formatted = formatter.print(duration.toPeriod());
        System.out.println(message.getJSONString() + formatted);
        connections.remove(message.getUser());
        System.out.println(connections.toString());
      }

      else {

        InetAddress IPAddress = receivePacket.getAddress();

        int port = receivePacket.getPort();

        String capitalizedSentence = message.getMessage().toUpperCase();

        sendData = capitalizedSentence.getBytes();

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
