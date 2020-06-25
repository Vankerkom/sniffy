package be.vankerkom.sniffy.events;

import lombok.Value;

import java.util.UUID;

@Value
public class SessionCreateEvent {
    UUID id;
    String name;
}
