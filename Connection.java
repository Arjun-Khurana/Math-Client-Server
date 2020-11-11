import java.net.InetAddress;
import java.time.Instant;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.Duration;

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

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public String getFormattedTime() {
        long difference = end.toEpochMilli() - start.toEpochMilli();
        Duration duration = new Duration(difference);
        PeriodFormatter formatter = PeriodFormat.getDefault();
        return formatter.print(duration.toPeriod());
    }
}