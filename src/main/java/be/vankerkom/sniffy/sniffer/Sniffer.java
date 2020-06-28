package be.vankerkom.sniffy.sniffer;

import be.vankerkom.sniffy.model.Protocol;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.*;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.TransportPacket;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
public class Sniffer extends Thread {

    // Sniffer configuration.
    private static final int SNAPSHOT_LENGTH = 65536;
    private static final int READ_TIMEOUT = 10;

    private final PcapNetworkInterface networkInterface;
    private final Protocol protocol;

    private PcapHandle pcapHandle;

    @Override
    public void run() {
        try (final var handle = openLiveHandle();
        /*final var dumper = handle.dumpOpen("dumps/" + LocalDateTime.now().toString() + ".pcapFile")*/) {
            this.pcapHandle = handle;
            applyFilter(handle);

            // TODO Rewrite this so it's breakable. Using Loop?
            // This currently hangs the thread.

            while (handle.isOpen()) {
                final var packet = handle.getNextPacket();

                if (packet == null || !packet.contains(IpPacket.class) || !packet.contains(TransportPacket.class)) {
                    continue;
                }

                final var timestamp = handle.getTimestamp();

                final var ipPacket = packet.get(IpPacket.class);

                log.info("ipPacket: {}", ipPacket);

                //dumper.dump(ipPacket, timestamp);
            }

        } catch (PcapNativeException | NotOpenException e) {
            log.error("Sniffing error", e);
        } finally {
            this.pcapHandle = null;
        }
    }

    private PcapHandle openLiveHandle() throws PcapNativeException {
        return networkInterface.openLive(
                SNAPSHOT_LENGTH,
                PcapNetworkInterface.PromiscuousMode.PROMISCUOUS,
                READ_TIMEOUT
        );
    }

    private void applyFilter(PcapHandle handle) throws PcapNativeException, NotOpenException {
        final String filter = protocol.getFilter();

        if (!filter.isBlank()) {
            handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
        }
    }

    public void shutdown() {
        if (pcapHandle != null) {
            try {
                pcapHandle.breakLoop();
            } catch (NotOpenException e) {
                // Gracefully shutdown.
            }
        }
    }


    public boolean isActive() {
        return false;
    }
}
