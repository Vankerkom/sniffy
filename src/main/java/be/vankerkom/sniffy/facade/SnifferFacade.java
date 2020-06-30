package be.vankerkom.sniffy.facade;

import be.vankerkom.sniffy.dto.DeviceDto;
import be.vankerkom.sniffy.dto.SnifferDto;
import be.vankerkom.sniffy.dto.SnifferStartRequest;
import be.vankerkom.sniffy.mappers.DeviceMapper;
import be.vankerkom.sniffy.services.DeviceService;
import be.vankerkom.sniffy.services.ProtocolService;
import be.vankerkom.sniffy.services.SnifferService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class SnifferFacade {

    private final ProtocolService protocolService;
    private final SnifferService snifferService;
    private final DeviceService deviceService;

    private final DeviceMapper deviceMapper;

    @SneakyThrows
    public void startSniffing(SnifferStartRequest request) {
        final var protocol = protocolService.getById(request.getProtocolId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown protocol id: " + request.getProtocolId()));

        snifferService.start(request.getInterfaceName(), protocol);
    }

    public void stopSniffing() {
        snifferService.stop();
    }

    public List<DeviceDto> getSniffingDevices() {
        return deviceService.getAllDevices()
                .stream()
                .map(deviceMapper::toDto)
                .collect(toList());
    }

    public SnifferDto getState() {
        return SnifferDto.builder()
                .active(snifferService.isActive())
                .build();
    }

}
