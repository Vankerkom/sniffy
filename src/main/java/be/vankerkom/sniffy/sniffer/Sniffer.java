package be.vankerkom.sniffy.sniffer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;

@RequiredArgsConstructor
@Slf4j
public class Sniffer extends Thread {

    private final PcapHandle handle;
    private final PacketListener packetListener;

    private boolean shutdown = false;

    @Override
    public void run() {
        try {
            handle.loop(-1, packetListener);
            shutdown = true;
        } catch (InterruptedException e) {
            log.error("Thread interrupted", e);
        } catch (PcapNativeException | NotOpenException e) {
            log.error("Sniffing error", e);
        }
    }

    public void shutdown() {
        if (handle != null) {
            try {
                handle.breakLoop();
            } catch (NotOpenException e) {
                // Gracefully shutdown.
            }
        }
    }

    public boolean isActive() {
        return shutdown || (handle != null && handle.isOpen());
    }

}
