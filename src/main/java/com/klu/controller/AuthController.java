package com.klu.controller;

import com.klu.dto.ApiResponse;
import com.klu.dto.LoginRequest;
import com.klu.dto.LoginResponse;
import com.klu.dto.RegisterRequest;
import com.klu.entity.User;
import com.klu.service.AuthService;
import com.klu.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // ✅ explicitly allow all
public class AuthController {

	@Autowired
	private AuthService service;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		try {
			String message = service.register(request);
			return ResponseEntity.ok(new ApiResponse(true, message));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			User user = service.login(request);
			String token = jwtUtil.generateToken(user.getEmail());

			LoginResponse response = new LoginResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), token);

			return ResponseEntity.ok(response);

		} catch (RuntimeException e) {
			return ResponseEntity.status(401).body(new ApiResponse(false, e.getMessage()));
		}
	}
}