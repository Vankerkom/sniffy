package be.vankerkom.sniffy.services;

import be.vankerkom.sniffy.events.MessagePacketReceivedEvent;
import be.vankerkom.sniffy.events.SnifferStateChanged;
import be.vankerkom.sniffy.model.Protocol;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TransportPacket;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PacketProcessingService implements PacketListener {

    private final ApplicationEventPublisher publisher;

    private Protocol protocol;

    @EventListener
    private void setProtocolOnStart(SnifferStateChanged event) {
        if (event.isActive()) {
            this.protocol = event.getProtocol();
            log.info("Setting protocol to {}", this.protocol);
        }
    }

    @Override
    public void gotPacket(Packet packet) {
        if (packet == null || !packet.contains(IpPacket.class) || !packet.contains(TransportPacket.class)) {
            return;
        }

        final var ipPacket = packet.get(IpPacket.class);
        final var transportPacket = packet.get(TransportPacket.class);
        final var inbound = protocol.isInbound(transportPacket.getHeader().getSrcPort());
        final var payload = transportPacket.getRawData();
        final var timestamp = LocalDateTime.now();

        log.info("ipPacket: {}", ipPacket);
        log.info("Protocol: {}", this.protocol);
        log.info("Inbound: {}", inbound);


        publisher.publishEvent(MessagePacketReceivedEvent.of(0L, timestamp, inbound, payload));
    }

}
