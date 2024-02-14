import nz.ac.wgtn.swen301.assignment2.MemAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;
public class MemAppenderTest {
    private MemAppender memAppender;
    private Logger logger = Logger.getLogger(MemAppenderTest.class);
    @BeforeEach
    public void setUp() {
        memAppender = new MemAppender();
    }

    @Test
    public void testLogEventRecording() {
        logger.addAppender(memAppender);
        logger.info("Test log message");
        var logs = memAppender.getCurrentLogs();
        assertFalse(logs.isEmpty());
        LoggingEvent loggedEvent = logs.get(0);
        assertEquals(logger.getName(), loggedEvent.getLoggerName());
        assertEquals(Level.INFO, loggedEvent.getLevel());
        assertEquals("Test log message", loggedEvent.getRenderedMessage());
    }
    @Test
    public void testDiscardedLogCount() {
        memAppender.setMaxSize(2);
        logger.addAppender(memAppender);
        logger.info("Log 1");
        logger.info("Log 2");
        logger.info("Log 3");
        long discardedCount = memAppender.getDiscardedLogCount();
        assertEquals(2, discardedCount);
    }

    @Test
    public void testLogCount() {
        memAppender.setMaxSize(100);
        logger.addAppender(memAppender);
        logger.info("Log 1");
        logger.info("Log 2");
        logger.info("Log 3");
        long logCount = memAppender.getLogCount();
        assertEquals(3, logCount);
    }

    @Test
    public void testGetLogs() {
        logger.addAppender(memAppender);
        logger.info("INFO");
        logger.debug("DEBUG");
        String [] discardedCount = memAppender.getLogs();
        assertEquals(memAppender.getCurrentLogs().size(),discardedCount.length);
    }
    @Test
    public void testNameProperty() {
        memAppender.setName("NewName");
        assertEquals("NewName", memAppender.getNameAppender());
    }
    @Test
    public void testMaxSizeProperty() {
        assertEquals(1000, memAppender.getMaxSize());
        memAppender.setMaxSize(500);
        assertEquals(500, memAppender.getMaxSize());
    }
    @Test
    public void testExportToJSON() throws IOException {
        logger.addAppender(memAppender);
        logger.debug("Debug message");
        logger.info("Information");
        memAppender.exportToJSON("FileName.json");
        String jsonContent = Files.readString(Paths.get("FileName.json"));
        JSONArray jsonArray = new JSONArray(jsonContent);

        assertEquals(2, jsonArray.length());

        JSONObject log1 = jsonArray.getJSONObject(0);
        JSONObject log2 = jsonArray.getJSONObject(1);

        assertEquals(logger.getName(), log1.get("name"));
        assertEquals(logger.getName(), log2.get("name"));

        assertEquals("DEBUG", log1.get("level"));
        assertEquals("Debug message", log1.get("message"));

        assertEquals("INFO", log2.get("level"));
        assertEquals("Information", log2.get("message"));
    }

    @Test
    public void testRequireLayout(){
        assertFalse(memAppender.requiresLayout());
    }
    @Test
    public void testActiveOption(){ memAppender.activateOptions();}
}
