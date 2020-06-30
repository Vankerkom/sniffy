package be.vankerkom.sniffy.services;

import lombok.SneakyThrows;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import java.util.List;

public class DeviceService {

    @SneakyThrows
    public List<PcapNetworkInterface> getAllDevices() {
        return Pcaps.findAllDevs();
    }

}
