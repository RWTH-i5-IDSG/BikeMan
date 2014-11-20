package de.rwth.idsg.bikeman.ixsi.processor;

import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionStore;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 04.11.2014
 */
public abstract class AbstractSubscriptionStore implements SubscriptionStore {

    // Want to get the logger of the extending class and not of this abstract one
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Key   (String)      = ID of the item
     * Value (Set<String>) = IDs of the subscribed systems
     */
    ConcurrentHashMap<String, Set<String>> lookupTable;

    /**
     * Service to remove expired subscriptions
     */
    ScheduledExecutorService scheduler;

    @Override
    public void subscribe(String systemID, List<String> itemIDs, Integer expireIntervalinMinutes) {
        subscribe(systemID, itemIDs);
        scheduleRemove(systemID, itemIDs, expireIntervalinMinutes);
        log.debug("System '{}' subscribed to '{}'. This subscription is scheduled to expire in {} minutes",
                systemID, itemIDs, expireIntervalinMinutes);
    }

    @Override
    public void subscribe(String systemID, List<String> itemIDs) {
        subscribeInternal(systemID, itemIDs);
        log.debug("System '{}' subscribed to '{}'", systemID, itemIDs);
    }

    private void subscribeInternal(String systemID, List<String> itemIDs) {
        Set<String> systemIDSet;
        for (String itemID : itemIDs) {
            systemIDSet = lookupTable.get(itemID);

            if (systemIDSet == null) {
                systemIDSet = new HashSet<>();
                systemIDSet.add(systemID);
                lookupTable.put(itemID, systemIDSet);

            } else {
                systemIDSet.add(systemID);
            }
        }
    }

    @Override
    public void unsubscribe(String systemID, List<String> itemIDs) {
        Set<String> systemIDSet;
        for (String itemID : itemIDs) {
            systemIDSet = lookupTable.get(itemID);

            if (systemIDSet != null) {
                systemIDSet.remove(systemID);
            }
        }
        log.debug("System '{}' unsubscribed from '{}'", systemID, itemIDs);
    }

    @Override
    public Set<String> getSubscribedSystems(String itemID) {
        return lookupTable.get(itemID);
    }

    @Override
    public List<String> getSubscriptions(String systemID) {
        List<String> subscriptions = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : lookupTable.entrySet()) {
            if (entry.getValue().contains(systemID)) {
                subscriptions.add(entry.getKey());
            }
        }
        return subscriptions;
    }

    @Override
    public void clear() {
        lookupTable.clear();
        log.debug("Cleared the subscription store");
    }

    @Override
    public String toString() {
        return lookupTable.toString();
    }

    // -------------------------------------------------------------------------
    // Schedule to remove subscriptions
    // -------------------------------------------------------------------------

    private void scheduleRemove(String systemID, List<String> itemIDs, long expireIntervalinMinutes) {
        scheduler.schedule(new RemoveJob(systemID, itemIDs), expireIntervalinMinutes, TimeUnit.MINUTES);
    }

    @RequiredArgsConstructor
    private class RemoveJob implements Runnable {

        private final String systemID;
        private final List<String> itemIDs;

        @Override
        public void run() {
            unsubscribe(systemID, itemIDs);
        }
    }
}
