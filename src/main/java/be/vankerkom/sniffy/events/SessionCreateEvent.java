package be.vankerkom.sniffy.events;

import lombok.Value;

@Value
public class SessionCreateEvent implements WebSocketEvent {

    @Override
    public EventId getEventId() {
        return EventId.SESSION_CREATE;
    }

    long id;
    String name;
}
