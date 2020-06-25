package be.vankerkom.sniffy.events;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface WebSocketEvent {

    @JsonIgnore
    default EventId getEventId() {
        return EventId.UNKNOWN;
    }

}
