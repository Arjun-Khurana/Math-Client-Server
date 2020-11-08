import java.sql.Time;
import org.joda.time.LocalDateTime;

public class Request {
    private Connection connection;
    private String mathRequest;
    private LocalDateTime timeRequested;

    public Request(Connection connection, String mathRequest, LocalDateTime timeRequested) {
        this.connection = connection;
        this.mathRequest = mathRequest;
        this.timeRequested = timeRequested;
    }
}