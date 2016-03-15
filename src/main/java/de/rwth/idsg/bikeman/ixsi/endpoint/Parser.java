package de.rwth.idsg.bikeman.ixsi.endpoint;

import xjc.schema.ixsi.IxsiMessageType;

/**
 * Created by max on 08/09/14.
 */
public interface Parser {
    IxsiMessageType unmarshal(String str);
    String marshal(IxsiMessageType ixsi);
}
