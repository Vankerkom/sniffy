package be.vankerkom.sniffy.model;

import lombok.Value;
import org.pcap4j.packet.namednumber.Port;

import java.util.Set;

import static java.util.function.Predicate.isEqual;
import static java.util.stream.Collectors.joining;

@Value
public class Protocol {

    int id;
    String name;
    String description;
    Set<ProtocolPort> ports;
    boolean packetViewEnabled;
    boolean messageViewEnabled;

    public String getFilter() {
        final var filter = ports.stream()
                .map(ProtocolPort::toBpfFilter)
                .collect(joining(" and "));

        return filter.isBlank() ? "tcp and udp" : filter;
    }

    public boolean isInbound(final Port srcPort) {
        return ports.stream()
                .map(ProtocolPort::getPortValue)
                .anyMatch(isEqual(srcPort.valueAsInt()));
    }

}
