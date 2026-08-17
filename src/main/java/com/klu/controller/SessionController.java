package com.klu.controller;

import com.klu.dto.SessionDTO;
import com.klu.entity.Session;
import com.klu.entity.Role;
import com.klu.service.SessionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService service;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestBody Session session) {
        try {
            Session saved = service.book(session);
            return ResponseEntity.ok(modelMapper.map(saved, SessionDTO.class));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<SessionDTO> getAll() {
        return service.getAll().stream()
                .map(session -> modelMapper.map(session, SessionDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/mentor/{id}")
    public List<SessionDTO> getMentorSessions(@PathVariable Long id) {
        return service.getMentorSessions(id).stream()
                .map(session -> modelMapper.map(session, SessionDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/mentee/{id}")
    public List<SessionDTO> getMenteeSessions(@PathVariable Long id) {
        return service.getMenteeSessions(id).stream()
                .map(session -> modelMapper.map(session, SessionDTO.class))
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body,
                                          Authentication authentication) {
        try {
            Session session = service.findById(id);

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + Role.ADMIN.name()));
            boolean isSessionMentor = session.getMentor() != null
                    && authentication.getName().equalsIgnoreCase(session.getMentor().getEmail());

            if (!isAdmin && !isSessionMentor) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Collections.singletonMap("error", "Only the session mentor can update this session."));
            }

            Session updated = service.updateStatus(id, body.get("status"));
            return ResponseEntity.ok(modelMapper.map(updated, SessionDTO.class));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
