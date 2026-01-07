
package com.example.EWaste_Management_System.Controller;


import com.example.EWaste_Management_System.DTO.EwasteDTO;
import com.example.EWaste_Management_System.DTO.UserProfileDTO;
import com.example.EWaste_Management_System.Entity.Ewaste;
import com.example.EWaste_Management_System.Entity.User;
import com.example.EWaste_Management_System.Repository.EwasteRepository;
import com.example.EWaste_Management_System.Repository.UserRepository;
import com.example.EWaste_Management_System.Service.EwasteService;
import com.example.EWaste_Management_System.Service.UserService;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ewaste")
@RequiredArgsConstructor
public class EwasteController {
 private final EwasteService ewasteService;
    private final EwasteRepository ewasteRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/submit", consumes = {"multipart/form-data"})
    public ResponseEntity<?> submitRequest(
            Principal principal,
            @RequestParam String deviceType,
            @RequestParam String brand,
            @RequestParam String model,
            @RequestParam String condition,
            @RequestParam int quantity,
            @RequestParam String pickupAddress,
            @RequestParam String remarks,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {
        Ewaste saved = ewasteService.createRequestWithImage(
                principal.getName(), deviceType, brand, model, condition, quantity, pickupAddress, remarks, image
        );
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('USER')")
@GetMapping("/my-requests")
public ResponseEntity<List<EwasteDTO>> myRequests(Principal principal) {
    return ResponseEntity.ok(
        ewasteService.getMyRequestsWithImages(principal.getName())
    );
}

    
    

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/status/{id}")
    public ResponseEntity<?> getStatus(@PathVariable Long id, Principal principal) {
        var user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var ewaste = ewasteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!ewaste.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You cannot access another user's request");
        }
        return ResponseEntity.ok(ewaste.getStatus());
    }
    
    
    @GetMapping("/me")
@PreAuthorize("hasAnyRole('USER','ADMIN','PICKUP')")
public ResponseEntity<?> getCurrentUser(Principal principal) {
    User user = userService.getByEmail(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

    return ResponseEntity.ok(
        new UserProfileDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone()
        )
    );
}
}