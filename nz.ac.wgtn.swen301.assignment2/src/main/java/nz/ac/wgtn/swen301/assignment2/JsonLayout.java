package nz.ac.wgtn.swen301.assignment2;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonLayout extends Layout {
    private JSONObject json;
    public JsonLayout(){
        json = new JSONObject();
    }
    @Override
    public String format(LoggingEvent loggingEvent) {
        Date d = new Date(loggingEvent.timeStamp);
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mm'Z'");
        json.put("name", loggingEvent.getLoggerName());
        json.put("level", loggingEvent.getLevel().toString());
        json.put("timestamp",f.format(d));
        json.put("thread", loggingEvent.getThreadName());
        json.put("message", loggingEvent.getRenderedMessage());
        return json.toString(5);
    }
    @Override
    public boolean ignoresThrowable() {
        return false;
    }

    @Override
    public void activateOptions() {
    }
}