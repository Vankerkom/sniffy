package be.vankerkom.sniffy.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    @SneakyThrows
    public List<PcapNetworkInterface> getAllDevices() {
        return Pcaps.findAllDevs();
    }

}
