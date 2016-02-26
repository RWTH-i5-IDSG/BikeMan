package de.rwth.idsg.bikeman.ixsi;

/**
 * Root exception for all things Ixsi
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 23.10.2014
 */
public class IxsiProcessingException extends RuntimeException {
    private static final long serialVersionUID = -2601054343528584333L;

    public IxsiProcessingException(String message) {
        super(message);
    }

    public IxsiProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
