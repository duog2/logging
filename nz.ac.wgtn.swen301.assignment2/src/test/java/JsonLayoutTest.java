import nz.ac.wgtn.swen301.assignment2.JsonLayout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
public class JsonLayoutTest {
    private Logger logger = Logger.getLogger(JsonLayoutTest.class);
    private JsonLayout jsonLayout;
    @BeforeEach
    public void setUp() {
        jsonLayout = new JsonLayout();
    }
    @Test
    public void testFormatLogEvent1() {
        LoggingEvent logEvent = new LoggingEvent(logger.getName(), logger, System.currentTimeMillis(), Level.INFO, "INFORMATION", new Throwable());
        String jsonLog = jsonLayout.format(logEvent);
        JSONObject jsonObject = new JSONObject(jsonLog);
        assertNotNull(jsonObject);
        assertEquals(logEvent.getLoggerName(), jsonObject.get("name"));
        assertEquals(logEvent.getLevel().toString(), jsonObject.get("level"));
        assertEquals(logEvent.getThreadName(),jsonObject.get("thread"));
        assertEquals(logEvent.getMessage(), jsonObject.get("message"));
        Date d = new Date(logEvent.timeStamp);
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mm'Z'");
        assertEquals(f.format(d),jsonObject.get("timestamp"));
    }
    @Test
    public void testFormatLogEvent2() {
        LoggingEvent logEvent = new LoggingEvent("foo", logger, System.currentTimeMillis(), Level.DEBUG, "interesting", new Throwable());
        String jsonLog = jsonLayout.format(logEvent);
        JSONObject jsonObject = new JSONObject(jsonLog);

        assertNotNull(jsonObject);
        assertEquals(logEvent.getLoggerName(), jsonObject.get("name"));
        assertEquals(logEvent.getLevel().toString(), jsonObject.get("level"));

        assertEquals(logEvent.getThreadName(),jsonObject.get("thread"));
        assertEquals(logEvent.getMessage(), jsonObject.get("message"));

        Date d = new Date(logEvent.timeStamp);
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mm'Z'");
        assertEquals(f.format(d),jsonObject.get("timestamp"));
    }
    @Test
    public void testFormatLogEvent3() {
        LoggingEvent logEvent = new LoggingEvent("LoggerTest", logger, System.currentTimeMillis(), Level.WARN, "interesting", new Throwable());
        String jsonLog = jsonLayout.format(logEvent);
        JSONObject jsonObject = new JSONObject(jsonLog);

        assertNotNull(jsonObject);
        assertEquals(logEvent.getLoggerName(), jsonObject.get("name"));
        assertEquals(logEvent.getLevel().toString(), jsonObject.get("level"));

        assertEquals(logEvent.getThreadName(),jsonObject.get("thread"));
        assertEquals(logEvent.getMessage(), jsonObject.get("message"));

        Date d = new Date(logEvent.timeStamp);
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mm'Z'");
        assertEquals(f.format(d),jsonObject.get("timestamp"));
    }

    @Test
    public void IgnoreThrowable(){
        assertEquals(false, jsonLayout.ignoresThrowable());
    }
}
