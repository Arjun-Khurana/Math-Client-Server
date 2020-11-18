package myapp.src.main.java.mavenpackage;

import java.net.DatagramPacket;
import org.nfunk.jep.*;
import java.time.Instant;

/**
 * A Request class to represent individual math requests the server receives from the client
 */
public class Request implements Runnable {
    private Connection connection;
    private String expression;
    private Instant timeRequested;
    private Boolean done;
    private Double result;
    private DatagramPacket receivePacket;

    /**
     * <p>Constructor for Request.</p>
     *
     * @param connection
     * @param expression a math expression
     * @param timeRequested time the math expression request was received
     * @param receivePacket
     */
    public Request(Connection connection, String expression, Instant timeRequested, DatagramPacket receivePacket) {
        this.connection = connection;
        this.expression = expression;
        this.timeRequested = timeRequested;
        this.receivePacket = receivePacket;
        setDone(false);
    }

    /**
     * <p>Getter for the field <code>done</code>.</p>
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean getDone() {
        return done;
    }

    /**
     * <p>Setter for the field <code>done</code>.</p>
     *
     * @param done determines if the expression result has been calculated
     */
    public void setDone(Boolean done) {
        this.done = done;
    }

    /**
     * <p>Getter for the field <code>result</code>.</p>
     *
     * @return a {@link java.lang.Double} object.
     */
    public Double getResult() {
        return result;
    }

    /**
     * <p>Setter for the field <code>result</code>.</p>
     *
     * @param result a {@link java.lang.Double} object.
     */
    public void setResult(Double result) {
        this.result = result;
    }

    /**
     * <p>Getter for the field <code>receivePacket</code>.</p>
     *
     * @return a {@link java.net.DatagramPacket} object.
     */
    public DatagramPacket getReceivePacket() {
        return receivePacket;
    }

    /**
     * <p>Setter for the field <code>receivePacket</code>.</p>
     *
     * @param receivePacket
     */
    public void setReceivePacket(DatagramPacket receivePacket) {
        this.receivePacket = receivePacket;
    }

    // Calculates the result of the expression
    @Override
    public void run() {
        JEP jep = new JEP();
        jep.parseExpression(expression);
        setResult(jep.getValue());
        setDone(true);
    }
}
