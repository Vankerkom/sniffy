package be.vankerkom.sniffy.controllers;

import be.vankerkom.sniffy.dto.SnifferDto;
import be.vankerkom.sniffy.dto.SnifferStartRequest;
import be.vankerkom.sniffy.facade.SnifferFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sniffer")
@RequiredArgsConstructor
public class SnifferController {

    private final SnifferFacade snifferFacade;

    @GetMapping
    public SnifferDto getState() {
        return snifferFacade.getState();
    }

    @PostMapping(value = "/start")
    public void startSniffing(@RequestBody SnifferStartRequest request) {
        snifferFacade.startSniffing(request);
    }

    @PostMapping(value = "/stop")
    public void stopSniffing() {
        snifferFacade.stopSniffing();
    }

}
