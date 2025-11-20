
package com.example.EWaste_Management_System.Service;

import com.example.EWaste_Management_System.DTO.UserRegistrationDTO;
import com.example.EWaste_Management_System.Model.User;
import com.example.EWaste_Management_System.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
     @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Load JWT properties from application.properties
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    // ------------ GENERATE KEY FOR TOKEN ---------------
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    private String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ------------ REGISTRATION METHOD ----------------
    public String registerUser(UserRegistrationDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return "Email Already Exists";
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // hashed password

        userRepository.save(user);
        return "Registration Successful";
    }

    // ------------ LOGIN METHOD ----------------
    public String login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid Email"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        // Generate JWT token
        return generateToken(user.getEmail());
    }
    
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }
    public String updateUser(Long id,User updatedUser){
        Optional<User> existingUser = userRepository.findById(id);
        if(existingUser.isEmpty()){
            return "User not Found";
        }
        User user=existingUser.get();
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        
        userRepository.save(user);
        return "User details updated successfully";
    }
    
    public String deleteUser(Long id){
        if(!userRepository.existsById(id)){
            return "User not found";
        }
        userRepository.deleteById(id);
        return "User deleted successfully";
    }
    
}
