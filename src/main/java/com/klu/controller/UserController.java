package com.klu.controller;

import com.klu.dto.StatsDTO;
import com.klu.dto.UserDTO;
import com.klu.entity.User;
import com.klu.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/users")
    public List<UserDTO> getUsers(@RequestParam(required = false) String role) {
        return service.getUsers(role).stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    }

    @GetMapping("/stats")
    public StatsDTO getStats() {
        return service.getStats();
    }
}
