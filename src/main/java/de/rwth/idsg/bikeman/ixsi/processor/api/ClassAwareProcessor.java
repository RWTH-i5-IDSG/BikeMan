package de.rwth.idsg.bikeman.ixsi.processor.api;

/**
 * Instances implementing this interface are aware of the class type they are able to process.
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 22.02.2016
 */
public interface ClassAwareProcessor<T> extends Processor {

    Class<T> getProcessingClass();

}
