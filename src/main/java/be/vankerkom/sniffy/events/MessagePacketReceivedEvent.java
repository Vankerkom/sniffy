package be.vankerkom.sniffy.events;

import lombok.Value;

import java.time.LocalDateTime;

@Value(staticConstructor = "of")
public class MessagePacketReceivedEvent implements WebSocketEvent {

    @Override
    public EventId getEventId() {
        return EventId.MESSAGE_PACKET_RECEIVED;
    }

    long sessionId;
    LocalDateTime timestamp;
    boolean inbound;
    byte[] payload;

}
