package be.vankerkom.sniffy.controllers;

import be.vankerkom.sniffy.dto.Device;
import be.vankerkom.sniffy.mappers.DeviceMapper;
import be.vankerkom.sniffy.services.SnifferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/v1/devices")
@RequiredArgsConstructor
public class DevicesController {

    private final SnifferService snifferService;
    private final DeviceMapper deviceMapper;

    @GetMapping
    public List<Device> getAllNetworkDevices() {
        return snifferService.getAllDevices()
                .stream()
                .map(deviceMapper::toDto)
                .collect(toList());
    }

}
