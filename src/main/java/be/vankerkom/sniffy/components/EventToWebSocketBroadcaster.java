package be.vankerkom.sniffy.components;

import be.vankerkom.sniffy.events.WebSocketEvent;
import be.vankerkom.sniffy.services.EventWebSocketService;
import lombok.RequiredArgsConstructor;
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
    public void broadcastEvents(WebSocketEvent event) {
        final var message = new TextMessage(event.toString());
        eventWebSocketService.broadcast(message);
    }

}
