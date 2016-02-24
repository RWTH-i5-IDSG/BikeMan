package de.rwth.idsg.bikeman.ixsi.dispatcher;

import com.google.common.base.Preconditions;
import de.rwth.idsg.bikeman.ixsi.IxsiProcessingException;
import de.rwth.idsg.bikeman.ixsi.processor.api.ClassAwareProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.api.Processor;
import de.rwth.idsg.bikeman.ixsi.processor.api.StaticRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestMessageProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.api.SubscriptionRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.processor.api.UserRequestProcessor;
import de.rwth.idsg.ixsi.jaxb.RequestMessageGroup;
import de.rwth.idsg.ixsi.jaxb.StaticDataRequestGroup;
import de.rwth.idsg.ixsi.jaxb.SubscriptionRequestGroup;
import de.rwth.idsg.ixsi.jaxb.UserTriggeredRequestChoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 24.02.2016
 */
@Slf4j
@Component
public class ProcessorProviderImpl implements ProcessorProvider {

    @Autowired private ListableBeanFactory beanFactory;

    private final LookupMap<StaticDataRequestGroup, StaticRequestProcessor>
            queryStaticMap = new LookupMap<>("queryStaticMap");

    private final LookupMap<UserTriggeredRequestChoice, UserRequestProcessor>
            queryUserMap = new LookupMap<>("queryUserMap");

    private final LookupMap<SubscriptionRequestGroup, SubscriptionRequestProcessor>
            subscriptionRequestMap = new LookupMap<>("subscriptionRequestMap");

    private final LookupMap<RequestMessageGroup, SubscriptionRequestMessageProcessor>
            subscriptionRequestMessageMap = new LookupMap<>("subscriptionRequestMessageMap");

    @PostConstruct
    public void init() {
        Set<ClassAwareProcessor> allProcessorBeans = getProcessorBeans();

        queryStaticMap.putAll(getForProcessingClass(allProcessorBeans, StaticDataRequestGroup.class));
        queryUserMap.putAll(getForProcessingClass(allProcessorBeans, UserTriggeredRequestChoice.class));
        subscriptionRequestMap.putAll(getForProcessingClass(allProcessorBeans, SubscriptionRequestGroup.class));
        subscriptionRequestMessageMap.putAll(getForProcessingClass(allProcessorBeans, RequestMessageGroup.class));

        log.trace("Ready");
    }

    @Override
    public StaticRequestProcessor find(StaticDataRequestGroup s) {
        return queryStaticMap.get(s.getClass());
    }

    @Override
    public UserRequestProcessor find(UserTriggeredRequestChoice s) {
        return queryUserMap.get(s.getClass());
    }

    @Override
    public SubscriptionRequestProcessor find(SubscriptionRequestGroup s) {
        return subscriptionRequestMap.get(s.getClass());
    }

    @Override
    public SubscriptionRequestMessageProcessor find(RequestMessageGroup s) {
        return subscriptionRequestMessageMap.get(s.getClass());
    }

    /**
     * Some pure Java black magic right here. Through the ListableBeanFactory we can discover all bean instances of
     * the given class type. Since we have many, many processors for request/message groups it was becoming
     * maintenance hell to manually inject and add them to the corresponding lookup map.
     */
    private Set<ClassAwareProcessor> getProcessorBeans() {
        Map<String, Processor> beans = beanFactory.getBeansOfType(Processor.class);
        Collection<Processor> beanCollection = beans.values();

        HashSet<ClassAwareProcessor> actual = new HashSet<>(beanCollection.size());
        for (Processor bean : beanCollection) {
            actual.add((ClassAwareProcessor) bean);
        }
        return actual;
    }

    /**
     * Iterate through the ClassAwareProcessor set, and return only the processors that are able to process
     * objects that extend/implement the given base class.
     */
    @SuppressWarnings("unchecked")
    private <T, K extends ClassAwareProcessor> List<K> getForProcessingClass(Set<ClassAwareProcessor> input,
                                                                             Class<T> baseMessageClazz) {
        return input.stream()
                    .filter(p -> baseMessageClazz.isAssignableFrom(p.getProcessingClass()))
                    .map(p -> (K) p)
                    .collect(Collectors.toList());
    }

    /**
     * A simple wrapper around HashMap with minor modifications for our own use case. Since we have many, many
     * processors for request/message groups, it was a maintenance hell to manually set the class type of each
     * processor in the map. With the addition of ClassAwareProcessor, each processor can now provide this class
     * information.
     */
    @RequiredArgsConstructor
    private static class LookupMap<T, K extends ClassAwareProcessor> {
        private final String name;
        private final HashMap<Class<? extends T>, K> lookup = new HashMap<>();

        private K get(Class<? extends T> clazz) {
            K p = lookup.get(clazz);
            if (p == null) {
                throw new IxsiProcessingException(
                        "No processor is registered for the incoming request of type: " + clazz);
            } else {
                return p;
            }
        }

        @SuppressWarnings("unchecked")
        private void putAll(List<K> items) {
            for (K item : items) {
                lookup.put(Preconditions.checkNotNull(item.getProcessingClass()), item);
            }
            log();
        }

        private void log() {
            log.info("{} (size:{}) => {}", name, lookup.size(), lookup);
        }
    }
}
