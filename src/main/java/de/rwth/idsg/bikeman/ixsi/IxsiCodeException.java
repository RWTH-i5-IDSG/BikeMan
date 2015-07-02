package de.rwth.idsg.bikeman.ixsi;

import de.rwth.idsg.bikeman.ixsi.schema.ErrorCodeType;
import lombok.Getter;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 02.07.2015
 */
public class IxsiCodeException extends RuntimeException {
    private static final long serialVersionUID = -7596125695060986992L;

    @Getter private final ErrorCodeType errorCode;

    public IxsiCodeException(String message, ErrorCodeType errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public IxsiCodeException(String message, Throwable cause, ErrorCodeType errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

}
