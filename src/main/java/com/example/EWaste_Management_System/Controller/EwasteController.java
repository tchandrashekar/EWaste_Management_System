
package com.example.EWaste_Management_System.Controller;


import com.example.EWaste_Management_System.DTO.EwasteDTO;
import com.example.EWaste_Management_System.Entity.Ewaste;
import com.example.EWaste_Management_System.Repository.EwasteRepository;
import com.example.EWaste_Management_System.Repository.UserRepository;
import com.example.EWaste_Management_System.Service.EwasteService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ewaste")
@RequiredArgsConstructor
public class EwasteController {

    private final EwasteService ewasteService;
    private final EwasteRepository ewasteRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/submit")
    public ResponseEntity<?> submitRequest(Principal principal, @RequestBody EwasteDTO dto) {
        // use authenticated user email rather than userId from DTO
        String email = principal.getName();
        Ewaste saved = ewasteService.createRequest(email, dto.getDeviceType(), dto.getBrand(),
                dto.getModel(), dto.getCondition(), dto.getQuantity(), dto.getPickupAddress(), dto.getRemarks());
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-requests")
    public ResponseEntity<?> myRequests(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(ewasteService.getMyRequests(email));
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
}