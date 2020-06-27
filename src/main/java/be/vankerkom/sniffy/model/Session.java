package be.vankerkom.sniffy.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@Getter
public class Session {

    private final int id;
    private final String name;
    private final Protocol protocol;
    private final LocalDateTime startedAt;

    @Builder.Default
    private final List<TransportMessage> transportMessages = new ArrayList<>();

    public Session(int id, String name, Protocol protocol, LocalDateTime startedAt) {
        this.id = id;
        this.name = name;
        this.protocol = protocol;
        this.startedAt = startedAt;
    }

    public void addTransportMessage(TransportMessage packet) {
        this.transportMessages.add(packet);
    }

    public List<TransportMessage> getTransportMessages() {
        return unmodifiableList(transportMessages);
    }

}
