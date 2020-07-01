package be.vankerkom.sniffy.events;

import be.vankerkom.sniffy.model.Protocol;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;

@Value
public class SnifferStateChanged implements WebSocketEvent {

    @Override
    public EventId getEventId() {
        return EventId.SNIFFER_STATE_CHANGED;
    }

    boolean active;

    @JsonIgnore
    Protocol protocol;

}
