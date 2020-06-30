package be.vankerkom.sniffy.sniffer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TransportPacket;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PacketProcessor implements PacketListener {

    @Override
    public void gotPacket(Packet packet) {
        if (packet == null || !packet.contains(IpPacket.class) || !packet.contains(TransportPacket.class)) {
            return;
        }

        final var ipPacket = packet.get(IpPacket.class);

        log.info("ipPacket: {}", ipPacket);
    }

}
