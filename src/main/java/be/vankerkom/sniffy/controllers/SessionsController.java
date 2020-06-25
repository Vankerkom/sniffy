package be.vankerkom.sniffy.controllers;

import be.vankerkom.sniffy.dto.SessionDto;
import be.vankerkom.sniffy.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/sessions")
@RequiredArgsConstructor
public class SessionsController {

    private final SessionService sessionService;

    @GetMapping
    public List<SessionDto> getSessions() {
        return sessionService.getAllSessions();
    }

    @DeleteMapping("{sessionId}")
    public void deleteSession(@PathVariable Long sessionId) {
        sessionService.deleteSession(sessionId);
    }

}
