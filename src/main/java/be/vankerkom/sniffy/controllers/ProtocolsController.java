package be.vankerkom.sniffy.controllers;

import be.vankerkom.sniffy.dto.ProtocolDto;
import be.vankerkom.sniffy.mappers.ProtocolMapper;
import be.vankerkom.sniffy.services.ProtocolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/v1/protocols")
@RequiredArgsConstructor
public class ProtocolsController {

    private final ProtocolService protocolService;
    private final ProtocolMapper protocolMapper;

    @GetMapping
    public List<ProtocolDto> getProtocols() {
        return protocolService.getAll()
                .stream()
                .map(protocolMapper::toDto)
                .collect(toList());
    }

}
