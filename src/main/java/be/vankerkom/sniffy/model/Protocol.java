package be.vankerkom.sniffy.model;

import lombok.Value;

import java.util.Set;

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

}
