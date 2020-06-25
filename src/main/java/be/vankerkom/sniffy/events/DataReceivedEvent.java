package be.vankerkom.sniffy.events;

import lombok.Value;

import java.sql.Timestamp;

@Value
public class DataReceivedEvent implements WebSocketEvent {

    @Override
    public EventId getEventId() {
        return EventId.DATA_RECEIVED;
    }

    long sessionId;
    Timestamp timestamp;
    byte[] payload;
}
