package de.rwth.idsg.bikeman.ixsi.processor;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 04.11.2014
 */
public abstract class AbstractSubscriptionStore implements SubscriptionStore {

    /**
     * Key   (Long)         = ID of the item
     * Value (List<String>) = ID of the subscribed system
     */
    ConcurrentHashMap<Long, List<String>> lookupTable;

    /**
     * Service to remove expired subscriptions
     */
    ScheduledExecutorService scheduler;

    @Override
    public void subscribe(String systemID, List<Long> itemIDs, long expireIntervalinMinutes) {
        subscribe(systemID, itemIDs);
        scheduleRemove(systemID, itemIDs, expireIntervalinMinutes);
    }

    @Override
    public void subscribe(String systemID, List<Long> itemIDs) {
        for (Long itemID : itemIDs) {
            if (lookupTable.contains(itemID)) {
                lookupTable.get(itemID).add(systemID);
            } else {
                List<String> subscribedSystems = new ArrayList<>();
                subscribedSystems.add(systemID);
                lookupTable.put(itemID, subscribedSystems);
            }
        }
    }

    @Override
    public void unsubscribe(String systemID, List<Long> itemIDs) {
        for (Long itemID : itemIDs) {
            if (lookupTable.contains(itemID)) {
                lookupTable.get(itemID).remove(systemID);
            }
        }
    }

    @Override
    public List<String> getSubscribedSystems(Long itemID) {
        return lookupTable.get(itemID);
    }

    @Override
    public List<Long> getSubscriptions(String systemID) {
        List<Long> subscriptions = new ArrayList<>();
        for (Map.Entry<Long, List<String>> entry : lookupTable.entrySet()) {
            if (entry.getValue().contains(systemID)) {
                subscriptions.add(entry.getKey());
            }
        }
        return subscriptions;
    }

    // -------------------------------------------------------------------------
    // Schedule to remove subscriptions
    // -------------------------------------------------------------------------

    private void scheduleRemove(String systemID, List<Long> itemIDs, long expireIntervalinMinutes) {
        scheduler.schedule(new RemoveJob(systemID, itemIDs), expireIntervalinMinutes, TimeUnit.MINUTES);
    }

    @RequiredArgsConstructor
    private class RemoveJob implements Runnable {

        private final String systemID;
        private final List<Long> itemIDs;

        @Override
        public void run() {
            unsubscribe(systemID, itemIDs);
        }
    }
}
