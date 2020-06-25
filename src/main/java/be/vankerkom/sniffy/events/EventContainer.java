package be.vankerkom.sniffy.events;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class EventContainer {

    @JsonProperty("i")
    int id;

    @JsonProperty("d")
    Object payload;

    public static EventContainer of(WebSocketEvent event) {
        return new EventContainer(event.getEventId().getId(), event);
    }

}
