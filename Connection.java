import java.time.Instant;

public class Connection {
    private String username;
    private Instant start;
    private Instant end;

    public Connection(String username, Instant start) {
        this.username = username;
        this.start = start;
    }

    public String getUsername() {
        return username;
    }

    public Instant getStart() {
        return start;
    }
}