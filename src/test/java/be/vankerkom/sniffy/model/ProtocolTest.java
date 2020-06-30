package be.vankerkom.sniffy.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProtocolTest {

    @Test
    void getFilterWithNoPortsReturnsTcpAndUdpString() {
        final var protocol = new Protocol(0, "", "", Set.of());

        assertEquals("tcp and udp", protocol.getFilter());
    }

    @Test
    void getFilterWithUdpPortReturnsUdpFilter() {
        final var protocol = new Protocol(0, "", "", Set.of(ProtocolPort.udp(1337)));

        assertEquals("udp port 1337", protocol.getFilter());
    }

    @Test
    void getFilterWithUdpPortReturnsTcpFilter() {
        final var protocol = new Protocol(0, "", "", Set.of(ProtocolPort.tcp(9901)));

        assertEquals("tcp port 9901", protocol.getFilter());
    }

    @Test
    void getFilterMultiplePortsReturnsCombinedFilter() {
        final var protocol = new Protocol(0, "", "", Set.of(ProtocolPort.tcp(9901)));

        assertEquals("tcp port 9901", protocol.getFilter());
    }

}