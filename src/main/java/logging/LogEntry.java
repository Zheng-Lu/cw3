package logging;

import java.util.Map;
import java.util.stream.Collectors;

public class LogEntry {
    private String callername;
    private String result;
    private Map<String,String> additionalInfo;

    public LogEntry(String callername, Object result, Map<String,Object> additionalInfo){
        this.callername = callername;
        this.result = result.toString();
        this.additionalInfo = additionalInfo.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> String.valueOf(entry.getValue())));
    }

    public String getResult(){
        return result;
    }

}
