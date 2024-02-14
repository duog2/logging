package nz.ac.wgtn.swen301.assignment2;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import javax.management.*;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemAppender extends AppenderSkeleton implements MemAppenderMBean {
    List<LoggingEvent> loggingEventList;
    private String name;
    private long maxSize;
    private long DiscardLog = 0;
    private ObjectName objectName;
    private MBeanServer mbs;
    public MemAppender() {
        name = null;
        maxSize = 1000;
        loggingEventList = Collections.synchronizedList(new ArrayList<>());
        DiscardLog = 0;
        mbs = ManagementFactory.getPlatformMBeanServer();

    }
    public void setName(String name) {
        this.name = name;
        try {
            objectName = new ObjectName("nz.ac.wgtn.swen301.assignment2:type=basic,name=" + name);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }

        if (!mbs.isRegistered(objectName)) {
            try {
                mbs.registerMBean(this, objectName);
            } catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("MBean with the same ObjectName is already registered.");
        }
    }
    public void setMaxSize(long maxSize) { this.maxSize = maxSize; }
    public String getNameAppender() {
        return name;
    }
    public long getMaxSize(){
        return maxSize;
    }
    public List<LoggingEvent>getCurrentLogs(){ return Collections.unmodifiableList(loggingEventList); }
    public long getLogCount() { return loggingEventList.size(); }
    public long getDiscardedLogCount(){
        return DiscardLog;
    }
    public String[] getLogs() {
        String[] logs = new String[loggingEventList.size()];
        String format = PatternLayout.DEFAULT_CONVERSION_PATTERN;
        PatternLayout layout = new PatternLayout();
        layout.setConversionPattern(format);
        for (int i = 0; i < loggingEventList.size(); i++) {
            LoggingEvent event = loggingEventList.get(i);
            String logString = layout.format(event);
            logs[i] = logString;
        }
        return logs;
    }
    @Override
    public void append(LoggingEvent loggingEvent) {
        if(loggingEventList.size() < maxSize) {
            loggingEventList.add(loggingEvent);
        }
        if(loggingEventList.size() == maxSize){
            loggingEventList.remove(loggingEventList.get(0));
            DiscardLog +=1;
        }
    }
    @Override
    public void exportToJSON(String Filename) {
        try (FileWriter fileWriter = new FileWriter(Filename)){
            JsonLayout layout = new JsonLayout();
            fileWriter.write("[");
            for (int i = 0; i < loggingEventList.size(); i++) {
                LoggingEvent event = loggingEventList.get(i);
                String jsonLog = layout.format(event);
                fileWriter.write(jsonLog);
                if (i < loggingEventList.size() - 1) {
                    fileWriter.write(",");
                }
                fileWriter.write(System.lineSeparator());
            }
            fileWriter.write("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void close() {}
    @Override
    public boolean requiresLayout() {return false;}
}
