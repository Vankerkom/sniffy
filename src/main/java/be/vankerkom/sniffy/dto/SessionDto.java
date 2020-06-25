package be.vankerkom.sniffy.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SessionDto {

    private int id;
    private String name;
    private String address;
    private int port;

}
