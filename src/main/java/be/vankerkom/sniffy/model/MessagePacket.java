package be.vankerkom.sniffy.model;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class MessagePacket {

    UUID id;
    LocalDateTime timestamp;
    boolean inbound;
    byte[] payload;

}
