package de.rwth.idsg.bikeman.ixsi.processor;

import java.util.List;
import java.util.Set;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 04.11.2014
 */
public interface SubscriptionStore {
    void subscribe(String systemID, List<Long> itemIDs, Integer expireIntervalinMinutes);
    void subscribe(String systemID, List<Long> itemIDs);
    void unsubscribe(String systemID, List<Long> itemIDs);
    Set<String> getSubscribedSystems(Long itemID);
    List<Long> getSubscriptions(String systemID);
    void clear();
}