package be.vankerkom.sniffy.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class SessionDto {

    private int id;
    private String name;
    private int protocolId;
    private LocalDateTime startedAt;

}
