package be.vankerkom.sniffy.services;

import be.vankerkom.sniffy.events.SnifferStateChanged;
import be.vankerkom.sniffy.model.Protocol;
import be.vankerkom.sniffy.sniffer.Sniffer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.util.Optional.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SnifferService {

    private static final int SNAPSHOT_LENGTH = 65536;
    private static final int READ_TIMEOUT = 10;

    private final ApplicationEventPublisher publisher;
    private final PacketProcessingService packetProcessingService;

    private Sniffer sniffer;

    @SneakyThrows
    @PostConstruct
    public void createDumpsDirectory() {
        final var dumpsDirectory = Path.of("dumps");
        if (!Files.exists(dumpsDirectory)) {
            Files.createDirectory(dumpsDirectory);
        }
    }

    public void start(String interfaceName, Protocol protocol) {
        if (sniffer != null) {
            throw new IllegalStateException("Sniffer already active");
        }

        final var networkInterface = getNetworkInterfaceByName(interfaceName)
                .orElseThrow();

        final var handle = openLiveHandle(networkInterface, protocol)
                .orElseThrow();

        publisher.publishEvent(new SnifferStateChanged(true, protocol));

        this.sniffer = new Sniffer(handle, packetProcessingService);
        this.sniffer.start();
    }

    private Optional<PcapHandle> openLiveHandle(PcapNetworkInterface networkInterface, Protocol protocol) {
        try {
            final PcapHandle handle = networkInterface.openLive(
                    SNAPSHOT_LENGTH,
                    PcapNetworkInterface.PromiscuousMode.PROMISCUOUS,
                    READ_TIMEOUT
            );

            final String filter = protocol.getFilter();

            if (!filter.isBlank()) {
                handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
            }

            return of(handle);
        } catch (PcapNativeException | NotOpenException e) {
            log.error("Could not open sniffer handle", e);
        }

        return empty();
    }

    public void stop() {
        if (sniffer == null) {
            throw new IllegalStateException("Sniffer not active");
        }

        shutdownSnifferGracefully();
    }

    private Optional<PcapNetworkInterface> getNetworkInterfaceByName(String interfaceName) {
        try {
            return ofNullable(Pcaps.getDevByName(interfaceName));
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
        publisher.publishEvent(new SnifferStateChanged(false, null));
    }

    public boolean isActive() {
        return ofNullable(sniffer)
                .map(Sniffer::isActive)
                .orElse(false);
    }

}
