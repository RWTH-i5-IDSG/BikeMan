package de.rwth.idsg.bikeman.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 16.12.2014
 */
@Slf4j
@Getter
@Setter
public class ItemIdComparator<T> {

    private List<T> newList;
    private List<T> databaseList;

    // -------------------------------------------------------------------------

    public List<T> getForUpdate() {
        Set<T> intersection = new HashSet<>(newList);
        intersection.retainAll(databaseList);
        log.debug("forUpdate: {}", intersection.size());
        return new ArrayList<>(intersection);
    }

    public List<T> getForInsert() {
        Set<T> difference = new HashSet<>(newList);
        difference.removeAll(databaseList);
        log.debug("forInsert: {}", difference.size());
        return new ArrayList<>(difference);
    }

    public List<T> getForDelete() {
        Set<T> difference = new HashSet<>(databaseList);
        difference.removeAll(newList);
        log.debug("forDelete: {}", difference.size());
        return new ArrayList<>(difference);
    }
}
