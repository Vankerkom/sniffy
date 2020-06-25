package be.vankerkom.sniffy.controllers;

import be.vankerkom.sniffy.dto.DeviceDto;
import be.vankerkom.sniffy.facade.SnifferFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DevicesController {

    private final SnifferFacade snifferFacade;

    @GetMapping
    public List<DeviceDto> getAllNetworkDevices() {
        return snifferFacade.getSniffingDevices();
    }

}
