package be.vankerkom.sniffy.events;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value(staticConstructor = "of")
public class MessagePacketReceivedEvent implements WebSocketEvent {

    @Override
    public EventId getEventId() {
        return EventId.MESSAGE_PACKET_RECEIVED;
    }

    UUID id;
    long sessionId;
    LocalDateTime timestamp;
    boolean inbound;
    byte[] payload;

}
