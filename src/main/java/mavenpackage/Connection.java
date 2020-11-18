package myapp.src.main.java.mavenpackage;

import java.time.Instant;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.Duration;

/**
 * A Connection class to keep track of how long clients are attached to the server
 */
public class Connection {
    private String username;
    private Instant start;
    private Instant end;

    /**
     * <p>Constructor for Connection.</p>
     *
     * @param username identifies the client
     * @param start time of initial attachment to the server
     */
    public Connection(String username, Instant start) {
        this.username = username;
        this.start = start;
    }

    /**
     * <p>Getter for the field <code>username</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUsername() {
        return username;
    }

    /**
     * <p>Getter for the field <code>start</code>.</p>
     *
     * @return a {@link java.time.Instant} object.
     */
    public Instant getStart() {
        return start;
    }

    /**
     * <p>Getter for the field <code>end</code>.</p>
     *
     * @return a {@link java.time.Instant} object.
     */
    public Instant getEnd() {
        return end;
    }

    /**
     * <p>Setter for the field <code>end</code>.</p>
     *
     * @param end time when client sends a close connection request.
     */
    public void setEnd(Instant end) {
        this.end = end;
    }

    /**
     * Formats the time of how long the client is attached to the server
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFormattedTime() {
        String formattedTime = "Attached to the server for ";
        long difference = end.toEpochMilli() - start.toEpochMilli();
        Duration duration = new Duration(difference);
        PeriodFormatter formatter = PeriodFormat.getDefault();
        formattedTime += formatter.print(duration.toPeriod());
        return formattedTime;
    }
}
