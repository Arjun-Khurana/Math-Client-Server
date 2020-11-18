package myapp.src.main.java.mavenpackage;

import java.net.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A Server class to handle math requests from clients
 */

class server {

  private static List<Connection> connections;
  private static Queue<Request> requests = new LinkedList<>();

  /**
   * <p>main.</p>
   *
   * @param args
   * @throws java.lang.Exception if any.
   */
  public static void main(String args[]) throws Exception {

    DatagramSocket serverSocket = new DatagramSocket(9876);
    connections = new ArrayList<>();

    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];
    Instant first = null;

    // Listen for requests
    while (true) {
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      serverSocket.receive(receivePacket);

      Message message = new Message(new String(receivePacket.getData(), 0, receivePacket.getLength()));

      // Login: Add the client to the list
      if (message.getType().equals("login")) {
        // Server logs the details about the client.
        System.out.println(message.getJSONString());
        first = Instant.now();
        Connection c = new Connection(message.getUser(), first);
        connections.add(c);

        // send ack back to client
        Message ack = new Message("ack", c.getUsername(), "Welcome " + c.getUsername());
        sendData = ack.getJSONString().getBytes();
        serverSocket
            .send(new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort()));
      }

      // Logout request
      else if (message.getType().equals("logout")) {
        Instant second = Instant.now();
        Connection current = null;
        for (Connection c : connections) {
          if (c.getUsername().equals(message.getUser())) {
            current = c;
            c.setEnd(second);
          }
        }

        String formatted = current.getFormattedTime();
        System.out.println(message.getJSONString() + " " + formatted);
      }

      // Handle Math request and add to queue
      else {
        System.out.println(message.getJSONString());

        String expression = message.getMessage();

        // Find the connection from the list
        Connection current = null;
        for (Connection c : connections) {
          if (c.getUsername().equals(message.getUser())) {
            current = c;
          }
        }

        Request request = new Request(current, expression, Instant.now(), receivePacket);
        request.run();
        requests.add(request);
      }
      // Check if next math request from queue is complete and send response back
      if (!requests.isEmpty() && requests.peek().getDone()) {
        Request request = requests.remove();
        sendData = request.getResult().toString().getBytes();
        InetAddress IPAddress = request.getReceivePacket().getAddress();
        int port = request.getReceivePacket().getPort();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

        serverSocket.send(sendPacket);
      }
    }
  }
}
