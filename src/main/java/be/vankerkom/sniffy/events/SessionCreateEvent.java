package be.vankerkom.sniffy.events;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class SessionCreateEvent implements WebSocketEvent {

    @Override
    public EventId getEventId() {
        return EventId.SESSION_CREATE;
    }

    int id;
    String name;
    int protocolId;
    LocalDateTime startedAt;

}
