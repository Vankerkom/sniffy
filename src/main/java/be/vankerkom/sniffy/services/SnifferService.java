package be.vankerkom.sniffy.services;

import be.vankerkom.sniffy.dto.SnifferStartRequest;
import be.vankerkom.sniffy.events.DataReceivedEvent;
import be.vankerkom.sniffy.events.SnifferStateChanged;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class SnifferService {

    private static final int PORT = 19711;
    private static final String FILTER = "udp port ";
    private static final int SNAPSHOT_LENGTH = 65536;
    private static final int READ_TIMEOUT = 10;

    private final ApplicationEventPublisher publisher;

    private PcapHandle handle;

    @SneakyThrows
    @PostConstruct
    public void createDumpsDirectory() {
        final var dumpsDirectory = Path.of("dumps");
        if (!Files.exists(dumpsDirectory)){
            Files.createDirectory(dumpsDirectory);
        }
    }

    public void start(SnifferStartRequest request) throws PcapNativeException, NotOpenException, InterruptedException {
        if (handle != null && handle.isOpen()) {
            throw new IllegalStateException("Sniffer already active");
        }

        final var networkInterface = getAllDevices().stream()
                .filter(device -> device.getName().equalsIgnoreCase(request.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid network interface: " + request.getName()));

        handle = networkInterface.openLive(SNAPSHOT_LENGTH, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);
        // handle.setFilter(FILTER + PORT, BpfProgram.BpfCompileMode.OPTIMIZE);

        new SnifferThread(this, handle, PORT).start();

        publisher.publishEvent(new SnifferStateChanged(true));
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

    public void analyse(Timestamp timestamp, TransportPacket packet, int port) {
        final var packetPayload = packet.getPayload();

        if (packetPayload == null) {
            return;
        }

        final boolean inbound = packet.getHeader().getSrcPort().valueAsInt() == port;
        log.info("{}: [inbound: {}]: {}", timestamp, inbound, packet);

        // TODO Analyse data based on protocol.
        publisher.publishEvent(new DataReceivedEvent(0, timestamp, packet.getPayload().getRawData()));
    }
}
