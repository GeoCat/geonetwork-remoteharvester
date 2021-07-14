package net.geocat.dblogging.service;


import net.geocat.database.linkchecker.entities.LogbackLoggingEvent;
import net.geocat.database.linkchecker.entities.LogbackLoggingEventException;
import net.geocat.database.linkchecker.repos.LogbackLoggingEventExceptionRepo;
import net.geocat.database.linkchecker.repos.LogbackLoggingEventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class GetLogService {

    @Autowired
    LogbackLoggingEventRepo logbackLoggingEventRepo;

    @Autowired
    LogbackLoggingEventExceptionRepo logbackLoggingEventExceptionRepo;

    public List<LogLine> queryLogByProcessID(String processID) {
        List<LogbackLoggingEvent> events = logbackLoggingEventRepo.findByJmsCorrelationIdOrderByTimestmp(processID);
        List<LogLine> result = new ArrayList<>();
        for (LogbackLoggingEvent event : events) {
            result.add(create(event));
        }
        return result;
    }

    public LogLine create(LogbackLoggingEvent event) {
        LogLine result = new LogLine();
        result.when = (Instant.ofEpochSecond(event.timestmp / 1000)).toString();
        result.isException = (event.referenceFlag == 2) || (event.referenceFlag == 3);
        result.level = event.levelString;
        result.message = event.formattedMessage;
        result.processID = event.jmsCorrelationId;
        result.threadName = event.threadName;

        //exception
        if (result.isException) {
            List<LogbackLoggingEventException> exceptionlines = logbackLoggingEventExceptionRepo.findByEventIdOrderByI(event.eventId);
            String allEx = String.join("\n", exceptionlines.stream().map(LogbackLoggingEventException::getTraceLine).collect(Collectors.toList()));
            result.message += "\n" + allEx;
        }
        return result;
    }
}
