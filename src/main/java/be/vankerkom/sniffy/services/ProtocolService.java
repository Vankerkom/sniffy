package be.vankerkom.sniffy.services;

import be.vankerkom.sniffy.model.Protocol;
import be.vankerkom.sniffy.model.ProtocolPort;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Optional.ofNullable;

@Service
public class ProtocolService {

    private final Map<Integer, Protocol> protocols = new HashMap<>();

    public ProtocolService() {
        addProtocol(new Protocol(0, "ALL", "No filter", Set.of(), true, false));
        addProtocol(new Protocol(1, "Giants", "Direct Play 8", Set.of(ProtocolPort.udp(19711)), true, false));
        addProtocol(new Protocol(2, "DNS", "Raw DNS Queries", Set.of(ProtocolPort.udp(53)), true, false));
        addProtocol(new Protocol(3, "Minecraft", "Minecraft", Set.of(ProtocolPort.tcp(25565)), true, true));
    }

    private void addProtocol(Protocol protocol) {
        protocols.put(protocol.getId(), protocol);
    }

    public List<Protocol> getAll() {
        return new ArrayList<>(protocols.values());
    }

    public Optional<Protocol> getById(int protocolId) {
        return ofNullable(protocols.get(protocolId));
    }

    public Protocol getDefaultProtocol() {
        return protocols.get(0);
    }
}
