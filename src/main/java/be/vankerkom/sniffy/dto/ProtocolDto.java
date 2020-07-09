package be.vankerkom.sniffy.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProtocolDto {

    private final int id;
    private final String name;
    private final String description;
    private final boolean packetViewEnabled;
    private final boolean messageViewEnabled;

}
