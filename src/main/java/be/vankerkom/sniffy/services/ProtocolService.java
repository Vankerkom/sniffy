package be.vankerkom.sniffy.services;

import be.vankerkom.sniffy.model.Protocol;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ProtocolService {

    private final List<Protocol> protocols = new ArrayList<>();

    public ProtocolService() {
        protocols.add(new Protocol(UUID.randomUUID(), "Giants", "Direct Play 8 - UDP Port 19711", "udp port 19711"));
    }

    public List<Protocol> getAll() {
        return Collections.unmodifiableList(protocols);
    }

}
