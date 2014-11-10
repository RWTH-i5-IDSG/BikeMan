package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 06.11.2014
 */
public interface SystemValidator {
    String getSystemID(String ipAddress) throws DatabaseException;
    boolean validate(String systemID);
//    void create(String systemId, String ipAddress) throws DatabaseException;
}
