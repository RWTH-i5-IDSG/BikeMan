package de.rwth.idsg.bikeman.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 18.06.2015
 */
@RestController
@RequestMapping(value = "/commit", produces = MediaType.TEXT_PLAIN_VALUE)
@Slf4j
public class CommitResource {

    private String gitProperties;

    @PostConstruct
    private void init() {
        try {
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("git.properties");
                 InputStreamReader ist = new InputStreamReader(in, StandardCharsets.UTF_8);
                 BufferedReader br = new BufferedReader(ist)) {

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    builder.append(line).append(System.getProperty("line.separator"));
                }
                gitProperties = builder.toString();
            }
        } catch (IOException e) {
            log.error("Exception occurred", e);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get() {
        return gitProperties;
    }
}
