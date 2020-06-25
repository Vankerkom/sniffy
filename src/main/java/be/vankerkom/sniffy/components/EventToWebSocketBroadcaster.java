package be.vankerkom.sniffy.components;

import be.vankerkom.sniffy.events.DataReceivedEvent;
import be.vankerkom.sniffy.services.EventWebSocketService;
import lombok.RequiredArgsConstructor;
import org.pcap4j.util.ByteArrays;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
@RequiredArgsConstructor
public class EventToWebSocketBroadcaster {

    private final EventWebSocketService eventWebSocketService;

    @Async
    @EventListener
    // TODO Add support for all events.
    public void broadcastEvents(DataReceivedEvent event) {
        final var data = ByteArrays.toHexString(event.getPayload(), " ");
        final var message = new TextMessage(data);
        eventWebSocketService.broadcast(message);
    }

}
