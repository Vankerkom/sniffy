package be.vankerkom.sniffy.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class MessagePacketDto {

    UUID id;
    int sessionId;
    LocalDateTime timestamp;
    boolean inbound;
    byte[] payload;

}