package be.vankerkom.sniffy.model;

import lombok.Value;
import org.pcap4j.packet.namednumber.Port;

import java.util.Set;
import java.util.function.Predicate;

import static java.util.function.Predicate.isEqual;
import static java.util.stream.Collectors.joining;

@Value
public class Protocol {

    int id;
    String name;
    String description;

    Set<ProtocolPort> ports;

    public String getFilter() {
        return ports.stream()
                .map(ProtocolPort::toBpfFilter)
                .collect(joining(" and "));
    }

    public boolean isInbound(final Port srcPort) {
        return ports.stream()
                .map(ProtocolPort::getPortValue)
                .anyMatch(isEqual(srcPort.valueAsInt()));
    }

}
