package be.vankerkom.sniffy.services;

import be.vankerkom.sniffy.dto.SnifferStartRequest;
import lombok.SneakyThrows;
import org.pcap4j.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.List;

@Service
public class SnifferService {

    private static final int PORT = 19711;
    private static final String FILTER = "udp port ";
    private static final int SNAPSHOT_LENGTH = 65536;
    private static final int READ_TIMEOUT = 10;

    private PcapHandle handle;

    public void start(SnifferStartRequest request) throws PcapNativeException, NotOpenException, InterruptedException {
        if (handle != null) {
            throw new IllegalStateException("Sniffer already active");
        }

        final var networkInterface = getAllDevices().stream()
                .filter(device -> device.getName().equalsIgnoreCase(request.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid network interface: " + request.getName()));

        handle = networkInterface.openLive(SNAPSHOT_LENGTH, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);
        handle.setFilter(FILTER + PORT, BpfProgram.BpfCompileMode.OPTIMIZE);
        // TODO Start handling the incoming packets on another thread.
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

        handle.close();
        handle = null;
    }

}
