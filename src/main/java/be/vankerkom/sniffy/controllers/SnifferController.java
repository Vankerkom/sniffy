package be.vankerkom.sniffy.controllers;

import be.vankerkom.sniffy.dto.SnifferStartRequest;
import be.vankerkom.sniffy.facade.SnifferFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sniffer")
@RequiredArgsConstructor
public class SnifferController {

    private final SnifferFacade snifferFacade;

    @PostMapping(value = "/start")
    public void startSniffing(@RequestBody SnifferStartRequest request) {
        snifferFacade.startSniffing(request);
    }

    @PostMapping(value = "/stop")
    public void stopSniffing() {
        snifferFacade.stopSniffing();
    }

}
