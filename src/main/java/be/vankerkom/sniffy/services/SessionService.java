package be.vankerkom.sniffy.services;

import be.vankerkom.sniffy.dto.SessionDto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SessionService {

    public List<SessionDto> getAllSessions() {
        return Collections.emptyList();
    }

    public void deleteSession(Long sessionId) {

    }
}
