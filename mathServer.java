import java.util.ArrayList;
import org.nfunk.jep.JEP;

class mathServer {
    private ArrayList<String> clients;
    public static void main(String[] args) throws Exception
    {
        // DatagramSocket serverSocket = new DatagramSocket(9876);
        org.nfunk.jep.JEP parser = new org.nfunk.jep.JEP();
        parser.parseExpression("1 + 5");
        double result = parser.getValue();
        System.out.println(result);
        
    }
}