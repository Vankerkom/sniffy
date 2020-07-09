package be.vankerkom.sniffy.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class MessageDto {

    UUID id;
    long sessionId;
    LocalDateTime timestamp;
    boolean inbound;
    byte[] payload;
    String protocol;
    int opcode;

}