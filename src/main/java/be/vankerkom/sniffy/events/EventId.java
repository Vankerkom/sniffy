package be.vankerkom.sniffy.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventId {

    UNKNOWN(0),
    SNIFFER_STATE_CHANGED(1),
    DATA_RECEIVED(2),
    SESSION_CREATE(3),
    SESSION_UPDATE(4),
    SESSION_DELETE(5),
    ;

    @Getter
    private final int id;

}
