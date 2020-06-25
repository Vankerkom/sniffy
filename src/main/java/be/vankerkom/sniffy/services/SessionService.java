package be.vankerkom.sniffy.services;

import be.vankerkom.sniffy.dto.SessionDto;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SessionService {

    private final Map<Integer, SessionDto> sessions = new HashMap<>();

    @PostConstruct
    void seedSessions() {
        addSession(SessionDto.builder().id(1).name("Test").build());
        addSession(SessionDto.builder().id(2).name("Test 2").build());
        addSession(SessionDto.builder().id(3).name("Test 3").build());
    }

    private void addSession(SessionDto test) {
        sessions.put(test.getId(), test);
    }

    public List<SessionDto> getAllSessions() {
        return List.copyOf(sessions.values());
    }

    public void deleteSession(Long sessionId) {
        // TODO
    }

    public SessionDto getSessionById(int sessionId) {
        return Optional.ofNullable(sessions.get(sessionId)).orElseThrow();
    }
}
