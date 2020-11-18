package myapp.src.main.java.mavenpackage;

import java.net.DatagramPacket;
import org.nfunk.jep.*;
import java.time.Instant;

public class Request implements Runnable {
    private Connection connection;
    private String expression;
    private Instant timeRequested;
    private Boolean done;
    private Double result;
    private DatagramPacket receivePacket;

    public Request(Connection connection, String expression, Instant timeRequested, DatagramPacket receivePacket) {
        this.connection = connection;
        this.expression = expression;
        this.timeRequested = timeRequested;
        this.receivePacket = receivePacket;
        setDone(false);
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public DatagramPacket getReceivePacket() {
        return receivePacket;
    }

    public void setReceivePacket(DatagramPacket receivePacket) {
        this.receivePacket = receivePacket;
    }

	@Override
	public void run() {
		JEP jep = new JEP();
        jep.parseExpression(expression);
        setResult(jep.getValue());
        setDone(true);
	}
}