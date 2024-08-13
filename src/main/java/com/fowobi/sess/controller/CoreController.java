package com.fowobi.sess.controller;

//import jakarta.servlet.http.HttpSession;
import com.fowobi.sess.dto.LoginRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CoreController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public FindByIndexNameSessionRepository<? extends Session> sessions;

    public CoreController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Collection<? extends Session> getSessions(Principal principal) {
        Collection<? extends Session> usersSessions = this.sessions.findByPrincipalName(principal.getName()).values();
        return usersSessions;
    }

    public Collection<? extends Session> getSessionsByName(String principal) {
        Collection<? extends Session> usersSessions = this.sessions.findByPrincipalName(principal).values();
        return usersSessions;
    }

    @GetMapping("/hello")
    public String hello(HttpSession httpSession) {
        log.info("http session: {}", httpSession.getId());
        log.info("http session: {}", httpSession);
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("http session: {}", name);
        Collection<? extends Session> userSessions = this.sessions.findByPrincipalName(name).values();
        log.info("number os sessions: {}", userSessions.size());
        log.info("Sessionsss: {}", userSessions.stream().filter(session -> session.isExpired()).collect(Collectors.toList()));

        return "hello world";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        return authentication == null ? "failed" : "success";
    }
}
