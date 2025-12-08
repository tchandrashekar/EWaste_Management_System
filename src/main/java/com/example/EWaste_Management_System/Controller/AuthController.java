
package com.example.EWaste_Management_System.Controller;

import com.example.EWaste_Management_System.DTO.LoginDTO;
import com.example.EWaste_Management_System.DTO.LoginResponseDTO;
import com.example.EWaste_Management_System.DTO.RegistrationDTO;
import com.example.EWaste_Management_System.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationDTO dto) {
        try {
            userService.registerUser(dto);
            return ResponseEntity.ok().body("Registration successful. Check email for verification.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        try {
            String token = userService.login(dto.getEmail(), dto.getPassword());
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam("token") String token) {
        boolean ok = userService.verifyToken(token);
        if (ok) return ResponseEntity.ok("Email verified. You may now login.");
        return ResponseEntity.badRequest().body("Invalid or expired token.");
    }
}