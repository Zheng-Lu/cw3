package logging;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Logger {
    private static Logger instance;
    private final List<LogEntry> log = new ArrayList<>();

    private Logger() {
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void logAction(String callerName, Object result) {
        LogEntry logentry = new LogEntry(callerName, result, null);
        this.log.add(logentry);
    }

    public void logAction(String callername, Object result, Map<String, Object> additionalInfo) {
        LogEntry logentry = new LogEntry(callername, result, additionalInfo);
        this.log.add(logentry);
    }

    public List<LogEntry> getLog() {
        return log;
    }

    public void clearLog() {
        log.clear();
    }
}
