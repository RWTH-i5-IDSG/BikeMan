package de.rwth.idsg.bikeman.app.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 23.02.2015
 */
@Getter
@Setter
@RequiredArgsConstructor
public class AppExceptionMessage {
    private final long timestamp;
    private final String code;
    private final String message;
}