package be.vankerkom.sniffy.sniffer;

import be.vankerkom.sniffy.services.SnifferService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapDumper;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TransportPacket;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
public class SnifferThread extends Thread {

    private final SnifferService snifferService;
    private final PcapHandle handle;

    @SneakyThrows
    @Override
    public void run() {
        final PcapDumper dumper = handle.dumpOpen("dumps/" + LocalDateTime.now().toString() + ".pcapFile");

        while (handle.isOpen()) {
            Packet packet = null;

            try {
                packet = handle.getNextPacket();
            } catch (NotOpenException e) {
                break; // Ignore and exit the loop.
            } catch (Exception e) {
                log.error("Error while sniffing", e);
            }

            if (packet == null || !packet.contains(IpPacket.class)) {
                continue;
            }

            final var transportPacket = packet.get(TransportPacket.class);

            if (transportPacket == null || transportPacket.getPayload() == null) {
                continue; // This might be a handshake?
            }

            final var timestamp = handle.getTimestamp();

            dumper.dump(packet, timestamp);

            try {
                snifferService.analyse(timestamp, transportPacket);
            } catch (Exception e) {
                log.error("Sniffing error", e);
            }
        }

        dumper.close();
    }
}
