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
    PACKET_RECEIVED(5),
    MESSAGE_RECEIVED(6),
    ;

    @Getter
    private final int id;

}
