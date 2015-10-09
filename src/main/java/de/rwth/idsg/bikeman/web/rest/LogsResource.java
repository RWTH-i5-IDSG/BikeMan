package de.rwth.idsg.bikeman.web.rest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import de.rwth.idsg.bikeman.web.rest.dto.LoggerDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for view and managing Log Level at runtime.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class LogsResource {

    private Path logPath;

    @PostConstruct
    private void init() {
        Optional<String> optionalPath = getActiveLogFile();
        if (optionalPath.isPresent()) {
            logPath = Paths.get(optionalPath.get());
        }
    }

    @Timed
    @RequestMapping(value = "/logs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LoggerDTO> getList() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        return context.getLoggerList()
                      .stream()
                      .map(LoggerDTO::new)
                      .collect(Collectors.toList());
    }

    @Timed
    @RequestMapping(value = "/logs", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeLevel(@RequestBody LoggerDTO jsonLogger) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger(jsonLogger.getName()).setLevel(Level.valueOf(jsonLogger.getLevel()));
    }

    /**
     * Return the application logs
     */
    @RequestMapping(value = "/logs/bikeman", method = RequestMethod.GET)
    public String log(HttpServletResponse response) {
        response.setContentType("text/plain");

        if (logPath == null) {
            return "Not available. Log file does not exist.";
        }

        String fileName = "bikeman.log";
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        response.setHeader(headerKey, headerValue);

        try (PrintWriter writer = response.getWriter()) {
            Files.lines(logPath, StandardCharsets.UTF_8)
                 .forEach(writer::println);
        } catch (IOException e) {
            log.error("Exception happened", e);
        }

        return null;
    }

    /**
     * Get the actively used log file from the underlying logging mechanism
     */
    private Optional<String> getActiveLogFile() {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        Iterator<Appender<ILoggingEvent>> itr = rootLogger.iteratorForAppenders();

        // Iterate over the appenders
        while (itr.hasNext()) {
            Appender<ILoggingEvent> appender = itr.next();

            // found a file appender
            if (appender instanceof FileAppender) {
                FileAppender<ILoggingEvent> fileAppender = (FileAppender<ILoggingEvent>) appender;
                return Optional.of(fileAppender.getFile());
            }
        }

        // no file appender
        return Optional.absent();
    }
}
