package be.vankerkom.sniffy.services;

import be.vankerkom.sniffy.events.DataReceivedEvent;
import be.vankerkom.sniffy.events.SnifferStateChanged;
import be.vankerkom.sniffy.model.Protocol;
import be.vankerkom.sniffy.sniffer.SnifferThread;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.*;
import org.pcap4j.packet.TransportPacket;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@Slf4j
@RequiredArgsConstructor
public class SnifferService {

    // Sniffer configuration.
    private static final int SNAPSHOT_LENGTH = 65536;
    private static final int READ_TIMEOUT = 10;

    private final SessionService sessionService;
    private final ApplicationEventPublisher publisher;

    private PcapHandle handle;

    @SneakyThrows
    @PostConstruct
    public void createDumpsDirectory() {
        final var dumpsDirectory = Path.of("dumps");
        if (!Files.exists(dumpsDirectory)) {
            Files.createDirectory(dumpsDirectory);
        }
    }

    public void start(String interfaceName, Protocol protocol) throws PcapNativeException, NotOpenException {
        if (handle != null && handle.isOpen()) {
            throw new IllegalStateException("Sniffer already active");
        }

        final var networkInterface = getNetworkInterfaceByName(interfaceName);

        setupInterface(protocol, networkInterface);

        // TODO Create multiple sessions.
        sessionService.createSession(protocol);

        new SnifferThread(this, handle).start();

        publisher.publishEvent(new SnifferStateChanged(true));
    }

    private void setupInterface(Protocol protocol, PcapNetworkInterface networkInterface) throws PcapNativeException, NotOpenException {
        handle = networkInterface.openLive(SNAPSHOT_LENGTH, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);

        if (!protocol.getFilter().isBlank()) {
            handle.setFilter(protocol.getFilter(), BpfProgram.BpfCompileMode.OPTIMIZE);
        }
    }

    private PcapNetworkInterface getNetworkInterfaceByName(String interfaceName) {
        return getAllDevices().stream()
                .filter(device -> device.getName().equalsIgnoreCase(interfaceName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid network interface: " + interfaceName));
    }

    public void stop() {
        if (handle == null) {
            throw new IllegalStateException("Sniffer not active");
        }

        shutdownSnifferGracefully();
    }

    @SneakyThrows
    public List<PcapNetworkInterface> getAllDevices() {
        return Pcaps.findAllDevs();
    }

    @PreDestroy
    public void shutdownSnifferGracefully() {
        if (handle == null) {
            return;
        }

        try {
            handle.breakLoop();
        } catch (NotOpenException e) {
            // Ignore
        }

        publisher.publishEvent(new SnifferStateChanged(false));

        handle.close();
        handle = null;
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
        return ofNullable(handle)
                .map(PcapHandle::isOpen)
                .orElse(false);
    }

}
