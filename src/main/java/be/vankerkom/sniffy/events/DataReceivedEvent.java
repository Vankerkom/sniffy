package be.vankerkom.sniffy.events;

import lombok.Value;

import java.sql.Timestamp;
import java.util.UUID;

@Value
public class DataReceivedEvent implements WebSocketEvent {
    UUID sessionId;
    Timestamp timestamp;
    byte[] payload;
}
