package be.vankerkom.sniffy.model;

import lombok.Value;

import java.sql.Timestamp;

@Value
public class TransportMessage {

    Timestamp timestamp;
    boolean inbound;
    byte[] payload;

}
