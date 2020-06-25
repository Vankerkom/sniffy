package be.vankerkom.sniffy.controllers;

import be.vankerkom.sniffy.dto.SnifferStartRequest;
import be.vankerkom.sniffy.services.SnifferService;
import lombok.RequiredArgsConstructor;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sniffer")
@RequiredArgsConstructor
public class SnifferController {

    private final SnifferService snifferService;

    @PostMapping(value = "/start")
    public void startSniffing(@RequestBody SnifferStartRequest request)
            throws PcapNativeException, InterruptedException, NotOpenException {
        snifferService.start(request);
    }

    @PostMapping(value = "/stop")
    public void stopSniffing() {
        snifferService.stop();
    }

}
