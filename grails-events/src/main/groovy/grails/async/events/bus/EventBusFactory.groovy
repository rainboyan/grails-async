package grails.async.events.bus

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.grails.async.events.bus.ExecutorEventBus

/**
 * Tries to create the default event bus
 *
 * @author Graeme Rocher
 * @since 3.3
 */
@CompileStatic
@Slf4j
class EventBusFactory {

    /**
     * @return Tries to auto discover and create the event bus
     */
    EventBus create() {
        List<EventBus> eventBuses = ServiceLoader.load(EventBus).toList()
        if(eventBuses.size() == 1) {
            EventBus eventBus = eventBuses.get(0)
            log.debug("Found event bus class to use [${eventBus.getClass().name}]")
            return eventBus
        }
        else if(eventBuses.size() > 1) {
            throw new IllegalStateException("More than one event bus implementation found on classpath ${eventBuses}. Remove one to continue.")
        }
        else {
            return createDefaultEventBus()
        }
    }

    protected EventBus createDefaultEventBus() {
        log.warn("No event bus implementations found on classpath, using synchronous implementation.")
        return new ExecutorEventBus()
    }
}
