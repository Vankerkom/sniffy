package be.vankerkom.sniffy.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Session {

    private final int id;
    private final String name;
    private final Protocol protocol;
    private final LocalDateTime startedAt;

    public Session(int id, String name, Protocol protocol, LocalDateTime startedAt) {
        this.id = id;
        this.name = name;
        this.protocol = protocol;
        this.startedAt = startedAt;
    }

}
