package be.vankerkom.sniffy.services;

import be.vankerkom.sniffy.dto.SessionDto;
import be.vankerkom.sniffy.mappers.SessionMapper;
import be.vankerkom.sniffy.model.Protocol;
import be.vankerkom.sniffy.model.Session;
import be.vankerkom.sniffy.model.SessionIdentifier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionMapper sessionMapper;
    private final ApplicationEventPublisher publisher;

    private final AtomicInteger newSessionId = new AtomicInteger();
    private final Map<Integer, Session> sessionsByIdMap = new HashMap<>();
    private final Map<SessionIdentifier, Session> sessionsByIdentifier = new HashMap<>();

    public List<SessionDto> getAllSessions() {
        return sessionMapper.toDtos(sessionsByIdMap.values());
    }

    public void deleteSession(Long sessionId) {
        // TODO
    }

    public SessionDto getSessionById(int sessionId) {
        return ofNullable(sessionsByIdMap.get(sessionId))
                .map(sessionMapper::toDto)
                .orElseThrow();
    }

    public Optional<Session> findSessionBySessionIdentifier(SessionIdentifier sessionIdentifier) {
        return ofNullable(sessionsByIdentifier.get(sessionIdentifier));
    }

    public Session createSession(Protocol protocol) {
        final var sessionId = newSessionId.incrementAndGet();
        final var timestamp = LocalDateTime.now();
        final var session = new Session(sessionId, sessionId + " - " + protocol.getName(), protocol, timestamp);

        sessionsByIdMap.put(sessionId, session);

        publisher.publishEvent(sessionMapper.toCreateEvent(session));

        return session;
    }

    public Session getOrCreateSession(SessionIdentifier sessionIdentifier, Protocol protocol) {
        return findSessionBySessionIdentifier(sessionIdentifier)
                .orElseGet(() -> createSessionWithIdentifier(sessionIdentifier, protocol));
    }

    private Session createSessionWithIdentifier(SessionIdentifier sessionIdentifier, Protocol protocol) {
        final var session = this.createSession(protocol);
        sessionsByIdentifier.put(sessionIdentifier, session);
        return session;
    }

}
