package be.vankerkom.sniffy.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventId {

    UNKNOWN(0),
    SNIFFER_STATE_CHANGED(1),
    SESSION_CREATE(2),
    SESSION_UPDATE(3),
    SESSION_DELETE(4),
    MESSAGE_PACKET_RECEIVED(5),
    ;

    @Getter
    private final int id;

}
