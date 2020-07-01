package be.vankerkom.sniffy.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class MessagePacket {

    LocalDateTime timestamp;
    boolean inbound;
    byte[] payload;

}
