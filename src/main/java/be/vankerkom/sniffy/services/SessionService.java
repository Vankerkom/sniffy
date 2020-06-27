package be.vankerkom.sniffy.services;

import be.vankerkom.sniffy.dto.SessionDto;
import be.vankerkom.sniffy.mappers.SessionMapper;
import be.vankerkom.sniffy.model.Protocol;
import be.vankerkom.sniffy.model.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionMapper sessionMapper;
    private final ApplicationEventPublisher publisher;

    private final AtomicInteger newSessionId = new AtomicInteger();
    private final Map<Integer, Session> sessions = new HashMap<>();

    public List<SessionDto> getAllSessions() {
        return sessionMapper.toDtos(sessions.values());
    }

    public void deleteSession(Long sessionId) {
        // TODO
    }

    public SessionDto getSessionById(int sessionId) {
        return Optional.ofNullable(sessions.get(sessionId))
                .map(sessionMapper::toDto)
                .orElseThrow();
    }

    public void createSession(Protocol protocol) {
        final var sessionId = newSessionId.incrementAndGet();
        final var timestamp = LocalDateTime.now();
        final var session = new Session(sessionId, sessionId + " - " + protocol.getName(), protocol, timestamp);

        sessions.put(sessionId, session);

        publisher.publishEvent(sessionMapper.toCreateEvent(session));
    }
}
