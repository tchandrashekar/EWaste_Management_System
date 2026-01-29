
package com.example.EWaste_Management_System.Service;

import com.example.EWaste_Management_System.DTO.PickupPersonDTO;
import com.example.EWaste_Management_System.DTO.RegistrationDTO;
import com.example.EWaste_Management_System.Entity.Role;
import com.example.EWaste_Management_System.Entity.User;
import com.example.EWaste_Management_System.Entity.VerificationToken;
import com.example.EWaste_Management_System.Repository.RoleRepository;
import com.example.EWaste_Management_System.Repository.UserRepository;
import com.example.EWaste_Management_System.Repository.VerificationTokenRepository;
import com.example.EWaste_Management_System.Security.JwtUtil;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

   // @Value("${app.base-url:http://localhost:8080}")
    @Value("${APP_BASE_URL}")
    private String appBaseUrl;

    @Transactional
    public User registerUser(RegistrationDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User u = new User();
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setPhone(dto.getPhone());
        u.setEnabled(false);

        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }
        u.getRoles().add(userRole);
        User saved = userRepository.save(u);

        // create verification token
        String token = UUID.randomUUID().toString();
        VerificationToken vt = new VerificationToken();
        vt.setToken(token);
        vt.setUser(saved);
        vt.setExpiryDate(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));
        tokenRepository.save(vt);

        // send email
        String link = appBaseUrl + "/api/auth/verify?token=" + token;
        String body = "Hi " + saved.getName() + ",\n\nPlease verify your email by clicking the link:\n" + link
                + "\n\nIf you did not sign up, ignore this email.\n\nThanks.";
        emailService.sendEmail(saved.getEmail(), "Verify your E-Waste account", body);

        return saved;
    }

    public boolean verifyToken(String token) {
        VerificationToken vt = tokenRepository.findByToken(token);
        if (vt == null) return false;
        if (vt.getExpiryDate().before(new Date())) {
            tokenRepository.delete(vt);
            return false;
        }
        User u = vt.getUser();
        u.setEnabled(true);
        userRepository.save(u);
        tokenRepository.delete(vt);
        return true;
    }

    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isEnabled()) {
            throw new RuntimeException("Please verify your email before login");
        }
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String roleName = user.getRoles().stream().findFirst().map(Role::getName).orElse("USER");
        return jwtUtil.generateToken(email, roleName);
    }

    public List<User> getAllUsers() { return userRepository.findAll(); }

    public Optional<User> getUserById(Long id) { return userRepository.findById(id); }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            user.setPhone(updatedUser.getPhone());
            user.setEnabled(updatedUser.isEnabled());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }
    
    @Transactional
public User createPickupPerson(PickupPersonDTO dto) {

    if (userRepository.existsByEmail(dto.getEmail())) {
        throw new RuntimeException("Email already exists");
    }

    User user = new User();
    user.setName(dto.getName());
    user.setEmail(dto.getEmail());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setPhone(dto.getPhone());
    user.setEnabled(true); // directly enabled

    Role pickupRole = roleRepository.findByName("PICKUP");
    if (pickupRole == null) {
        pickupRole = new Role();
        pickupRole.setName("PICKUP");
        roleRepository.save(pickupRole);
    }

    user.getRoles().add(pickupRole);
    return userRepository.save(user);
}

    
     public List<User> getPickupPersons() {
        return userRepository.findByRoles_Name("PICKUP");
    }
     
     public Optional<User> getByEmail(String email) {
    return userRepository.findByEmail(email);
}
}