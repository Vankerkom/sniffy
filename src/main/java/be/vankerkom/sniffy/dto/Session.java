package be.vankerkom.sniffy.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class Session {

    private UUID id;
    private String name;
    private String address;
    private int port;

}
