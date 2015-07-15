package de.rwth.idsg.bikeman.web.rest.dto.monitor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by max on 15/07/15.
 */
@Getter
@Setter
public class StoreDTO<T> {
    private String name;
    private String itemDescription;
    private String linkName;
    private int size;
    private List<StoreItem<T>> items;
}
