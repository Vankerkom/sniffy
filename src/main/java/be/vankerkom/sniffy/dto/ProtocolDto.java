package be.vankerkom.sniffy.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class ProtocolDto {

    private final UUID id;
    private final String name;
    private final String description;

}
