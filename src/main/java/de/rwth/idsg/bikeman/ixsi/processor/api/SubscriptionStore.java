package de.rwth.idsg.bikeman.ixsi.processor.api;

import java.util.List;
import java.util.Set;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 04.11.2014
 */
public interface SubscriptionStore {
    void subscribe(String systemID, List<String> itemIDs, Integer expireIntervalinMinutes);
    void subscribe(String systemID, List<String> itemIDs);
    void unsubscribe(String systemID, List<String> itemIDs);
    Set<String> getSubscribedSystems(String itemID);
    List<String> getSubscriptions(String systemID);
    void clear();
}