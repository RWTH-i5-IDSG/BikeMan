package de.rwth.idsg.bikeman.web.rest.dto.monitor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by max on 15/07/15.
 */
@Getter
@RequiredArgsConstructor
public class StoreItem<T> {
    private final String systemId;
    private final T item;
}
