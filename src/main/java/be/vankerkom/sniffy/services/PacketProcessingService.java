package be.vankerkom.sniffy.services;

import be.vankerkom.sniffy.events.MessagePacketReceivedEvent;
import be.vankerkom.sniffy.events.SnifferStateChanged;
import be.vankerkom.sniffy.model.Protocol;
import be.vankerkom.sniffy.model.Session;
import be.vankerkom.sniffy.model.SessionIdentifier;
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
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PacketProcessingService implements PacketListener {

    private final SessionService sessionService;
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

        final Session session = getSessionByPacketHeaders(ipPacket, transportPacket, inbound);

        final var payload = transportPacket.getRawData();
        final var timestamp = LocalDateTime.now();

        // Pipeline
        // Start a new session for the ip, port and protocol.

        log.debug("ipPacket: {}", ipPacket);
        log.debug("Protocol: {}", this.protocol);
        log.debug("Inbound: {}", inbound);
        log.debug("Session Id: {}", session.getId());

        // Check for application data/protocol decoders.
        // If no decoders present, log raw payload of the transport packet. //  and start a session for a port.

        publisher.publishEvent(MessagePacketReceivedEvent.of(UUID.randomUUID(), session.getId(), timestamp, inbound, payload));
    }

    private Session getSessionByPacketHeaders(IpPacket ipPacket, TransportPacket transportPacket, boolean inbound) {
        final var sessionIdentifier = getSessionIdentifier(inbound, ipPacket.getHeader(), transportPacket.getHeader());
        return getOrCreateSession(sessionIdentifier);
    }

    private SessionIdentifier getSessionIdentifier(boolean inbound, IpPacket.IpHeader ipHeader, TransportPacket.TransportHeader transportHeader) {
        return SessionIdentifier.of(inbound, ipHeader, transportHeader);
    }

    private Session getOrCreateSession(SessionIdentifier sessionIdentifier) {
        return sessionService.getOrCreateSession(sessionIdentifier, protocol);
    }

}
