package be.vankerkom.sniffy.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SnifferStartRequest {

    private String interfaceName;
    private int protocolId;

}
