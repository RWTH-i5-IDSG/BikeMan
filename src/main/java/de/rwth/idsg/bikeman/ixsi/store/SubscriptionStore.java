package de.rwth.idsg.bikeman.ixsi.store;

import java.util.List;
import java.util.Set;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 04.11.2014
 */
public interface SubscriptionStore<T> {
    void subscribe(String systemID, List<T> itemIDs, long expireIntervalinMinutes);
    void subscribe(String systemID, List<T> itemIDs);
    void unsubscribe(String systemID, List<T> itemIDs);
    void unsubscribeAll(String systemID);
    Set<String> getSubscribedSystems(T itemID);
    List<T> getSubscriptions(String systemID);
    void clear();
}
