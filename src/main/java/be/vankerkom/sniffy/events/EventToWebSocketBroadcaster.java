package be.vankerkom.sniffy.events;

import be.vankerkom.sniffy.services.EventWebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventToWebSocketBroadcaster {

    private final EventWebSocketService eventWebSocketService;
    private final ObjectMapper objectMapper;

    @EventListener
    public void broadcastEvents(WebSocketEvent event) {
        eventToTextMessage(event)
                .ifPresent(eventWebSocketService::broadcast);
    }

    private Optional<TextMessage> eventToTextMessage(WebSocketEvent event) {
        if (EventId.UNKNOWN.equals(event.getEventId())) {
            log.warn("Sending event: {} with the id UNKNOWN", event.getClass().getName());
        }

        final var eventMessageContainer = EventContainer.of(event);

        try {
            final var textMessage = new TextMessage(objectMapper.writeValueAsString(eventMessageContainer));
            return Optional.of(textMessage);
        } catch (JsonProcessingException e) {
            log.debug("Cannot convert event to json", e);
        }

        return Optional.empty();
    }

}
