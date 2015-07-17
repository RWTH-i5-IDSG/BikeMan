package de.rwth.idsg.bikeman.ixsi.api;

import xjc.schema.ixsi.IxsiMessageType;

import javax.xml.bind.JAXBException;

/**
 * Created by max on 08/09/14.
 */
public interface Parser {
    IxsiMessageType unmarshal(String str) throws JAXBException;
    String marshal(IxsiMessageType ixsi) throws JAXBException;
}
