package be.vankerkom.sniffy.services;

import be.vankerkom.sniffy.events.DataReceivedEvent;
import be.vankerkom.sniffy.events.SnifferStateChanged;
import be.vankerkom.sniffy.model.Protocol;
import be.vankerkom.sniffy.sniffer.Sniffer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.TransportPacket;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;

import static java.util.Optional.ofNullable;

@Service
@Slf4j
@RequiredArgsConstructor
public class SnifferService {

    private final SessionService sessionService;
    private final ApplicationEventPublisher publisher;

    private Sniffer sniffer;

    @SneakyThrows
    @PostConstruct
    public void createDumpsDirectory() {
        final var dumpsDirectory = Path.of("dumps");
        if (!Files.exists(dumpsDirectory)) {
            Files.createDirectory(dumpsDirectory);
        }
    }

    public void start(String interfaceName, Protocol protocol) throws PcapNativeException, NotOpenException {
        if (sniffer != null) {
            throw new IllegalStateException("Sniffer already active");
        }

        final var networkInterface = getNetworkInterfaceByName(interfaceName);

        // TODO Create multiple sessions.
        sessionService.createSession(protocol);

        this.sniffer = new Sniffer(networkInterface, protocol);
        this.sniffer.start();

        publisher.publishEvent(new SnifferStateChanged(true));
    }

    public void stop() {
        if (sniffer == null) {
            throw new IllegalStateException("Sniffer not active");
        }

        shutdownSnifferGracefully();
    }

    private PcapNetworkInterface getNetworkInterfaceByName(String interfaceName) {
        try {
            return Pcaps.getDevByName(interfaceName);
        } catch (PcapNativeException e) {
            log.error("Cannot find network device: " + interfaceName, e);
            throw new IllegalArgumentException("Invalid network interface: " + interfaceName, e);
        }
    }

    @PreDestroy
    public void shutdownSnifferGracefully() {
        if (sniffer == null) {
            return;
        }

        sniffer.shutdown();
        sniffer = null;
        publisher.publishEvent(new SnifferStateChanged(false));
    }

    public void analyse(Timestamp timestamp, TransportPacket packet) {
        final var packetPayload = packet.getPayload();

        if (packetPayload == null) {
            return;
        }

        log.info("{}: {}", timestamp, packet);

        // TODO Analyse data based on protocol.
        publisher.publishEvent(new DataReceivedEvent(0, timestamp, packet.getPayload().getRawData()));
    }

    public boolean isActive() {
        return ofNullable(sniffer)
                .map(Sniffer::isActive)
                .orElse(false);
    }

}
