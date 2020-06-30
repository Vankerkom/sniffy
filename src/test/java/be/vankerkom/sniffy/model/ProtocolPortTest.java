package be.vankerkom.sniffy.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProtocolPortTest {

    @Test
    void toBpfFilterWithTcpFalseShouldSetUdpInBpfFilter() {
        final var protocolPort = ProtocolPort.udp(1337);
        Assertions.assertEquals("udp port 1337", protocolPort.toBpfFilter());
    }

    @Test
    void toBpfFilterWithTcpTrueShouldSetTcpInBpfFilter() {
        final var protocolPort = ProtocolPort.tcp(77);
        Assertions.assertEquals("tcp port 77", protocolPort.toBpfFilter());
    }

}