package logging;

import java.util.Map;
import java.util.stream.Collectors;

public class LogEntry {
    private final String callername;
    private final String result;
    private final Map<String, String> additionalInfo;

    public LogEntry(String callername, Object result, Map<String, Object> additionalInfo) {
        this.callername = callername;
        if (result == null) {
            this.result = "null";
        } else {
            this.result = result.toString();
        }
        this.additionalInfo = additionalInfo.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> String.valueOf(entry.getValue())));
    }

    public String getResult() {
        return result;
    }

}
