package be.vankerkom.sniffy.events;

import lombok.Value;

import java.util.UUID;

@Value
public class SessionCreateEvent implements WebSocketEvent {
    UUID id;
    String name;
}
