package be.vankerkom.sniffy.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SessionDto {

    private long id;
    private String name;
    private String address;
    private int port;

}
