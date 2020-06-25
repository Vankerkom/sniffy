package be.vankerkom.sniffy.events;

import lombok.Value;

@Value
public class SnifferStateChanged implements WebSocketEvent {

    @Override
    public EventId getEventId() {
        return EventId.SNIFFER_STATE_CHANGED;
    }

    boolean active;
}
