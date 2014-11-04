package de.rwth.idsg.bikeman.ixsi.processor;

import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 04.11.2014
 */
public interface SubscriptionStore {
    void subscribe(String systemID, List<Long> itemIDs, long expireIntervalinMinutes);
    void subscribe(String systemID, List<Long> itemIDs);
    void unsubscribe(String systemID, List<Long> itemIDs);
    List<String> getSubscribedSystems(Long itemID);
    List<Long> getSubscriptions(String systemID);
}