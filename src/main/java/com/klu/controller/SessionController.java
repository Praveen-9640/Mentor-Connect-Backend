package com.klu.controller;

import com.klu.dto.SessionDTO;
import com.klu.entity.Session;
import com.klu.service.SessionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin
public class SessionController {

    @Autowired
    private SessionService service;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/book")
    public SessionDTO book(@RequestBody Session session) {
        Session saved = service.book(session);
        return modelMapper.map(saved, SessionDTO.class);
    }

    @GetMapping
    public List<SessionDTO> getAll() {
        return service.getAll().stream()
                .map(session -> modelMapper.map(session, SessionDTO.class))
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/status")
    public SessionDTO updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Session updated = service.updateStatus(id, body.get("status"));
        return modelMapper.map(updated, SessionDTO.class);
    }
}