package de.rwth.idsg.bikeman.ixsi.processor;

/**
 * T1 is request type.
 * T2 is response type.
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.09.2014
 */
public interface Processor<T1, T2> {
    T2 process(T1 request);
}