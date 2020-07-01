package be.vankerkom.sniffy.events;

import lombok.Value;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Value(staticConstructor = "of")
public class MessagePacketReceivedEvent implements WebSocketEvent {

    @Override
    public EventId getEventId() {
        return EventId.DATA_RECEIVED;
    }

    long sessionId;
    LocalDateTime timestamp;
    boolean inbound;
    byte[] payload;

}
