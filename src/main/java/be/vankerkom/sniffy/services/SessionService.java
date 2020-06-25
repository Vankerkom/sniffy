package be.vankerkom.sniffy.services;

import be.vankerkom.sniffy.dto.Session;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SessionService {

    public List<Session> getAllSessions() {
        return Collections.emptyList();
    }

    public void deleteSession(Long sessionId) {

    }
}
