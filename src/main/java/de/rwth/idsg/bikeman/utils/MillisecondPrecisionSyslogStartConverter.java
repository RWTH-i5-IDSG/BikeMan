/*
 * Copyright (c) 2012-2014 Spotify AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.rwth.idsg.bikeman.utils;

import ch.qos.logback.classic.pattern.SyslogStartConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.util.LevelToSyslogSeverity;
import ch.qos.logback.core.net.SyslogAppenderBase;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.io.IOException;

/**
 * Taken from: https://github.com/spotify/logging-java/tree/master/src/main/java/com/spotify/logging/logback
 *
 * Modified to use Joda-Time and ISO-8601 in order to meet the syslog timestamp format expectations
 *
 * A {@link SyslogStartConverter} with millisecond timestamp
 * precision.
 */
public class MillisecondPrecisionSyslogStartConverter extends SyslogStartConverter {

    private DateTimeFormatter isoFormatter;
    private String localHostName;
    private int facility;

    public void start() {
        int errorCount = 0;

        final String facilityStr = getFirstOption();
        if (facilityStr == null) {
            addError("was expecting a facility string as an option");
            return;
        }

        facility = SyslogAppenderBase.facilityStringToint(facilityStr);

        localHostName = getLocalHostname();
        try {
            isoFormatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .appendTimeZoneOffset("Z", true, 2, 4)
                .toFormatter();

        } catch (Exception e) {
            addError("Could not instantiate Joda DateTimeFormatter", e);
            errorCount++;
        }

        if (errorCount == 0) {
            super.start();
        }
    }

    public String convert(final ILoggingEvent event) {
        final StringBuilder sb = new StringBuilder();

        final int pri = facility + LevelToSyslogSeverity.convert(event);

        sb.append("<");
        sb.append(pri);
        sb.append(">");
        computeTimeStampString(sb, event.getTimeStamp());
        sb.append(' ');
        sb.append(localHostName);
        sb.append(' ');

        return sb.toString();
    }

    void computeTimeStampString(StringBuilder sb, final long now) {
        try {
            isoFormatter.printTo(sb, now);
        } catch (IOException e) {
            addError("Failed to convert timestamp to string", e);
        }
    }
}


