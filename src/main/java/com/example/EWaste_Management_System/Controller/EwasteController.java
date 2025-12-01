
package com.example.EWaste_Management_System.Controller;

import com.example.EWaste_Management_System.DTO.EwasteDTO;
import com.example.EWaste_Management_System.DTO.StatusUpdateDTO;
import com.example.EWaste_Management_System.Entity.Ewaste;
import com.example.EWaste_Management_System.Entity.Status;
import com.example.EWaste_Management_System.Entity.User;
import com.example.EWaste_Management_System.Repository.EwasteRepository;
import com.example.EWaste_Management_System.Repository.UserRepository;
import com.example.EWaste_Management_System.Service.EwasteService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/ewaste")
public class EwasteController {

    @Autowired
    private EwasteService ewasteService;

    @Autowired
    private EwasteRepository ewasteRepository;

    @Autowired
    private UserRepository userRepository;

    // USER submitting request
        @PostMapping("/submit")
      public ResponseEntity<?> submitRequest(@RequestBody EwasteDTO dto) {
          Ewaste saved = ewasteService.createEwaste(dto.getUserId(), dto);
          return ResponseEntity.ok(saved);
      }


    // USER get their own requests
    @GetMapping("/my-requests")
    public ResponseEntity<?> myRequests(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(ewasteService.getMyRequests(email));
    }

    // ADMIN create ewaste for a user
    @PostMapping("/{userId}")
    public Ewaste createEwaste(@PathVariable Long userId, @RequestBody EwasteDTO dto) {
        return ewasteService.createEwaste(userId, dto);
    }

    // ADMIN get user ewaste
    @GetMapping("/{userId}")
    public List<Ewaste> getUserEwaste(@PathVariable Long userId) {
        return ewasteService.getUserEwaste(userId);
    }

    // ADMIN get all ewaste
    @GetMapping
    public List<Ewaste> getAllEwaste() {
        return ewasteService.getAll();
    }

    // Request status
    @GetMapping("/status/{id}")
    public ResponseEntity<?> getStatus(@PathVariable Long id, Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ewaste ewaste = ewasteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!ewaste.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You cannot access another user's request");
        }

        return ResponseEntity.ok(ewaste.getStatus());
    }
    
    
  @PutMapping("/ewaste/update-status/{id}")
public ResponseEntity<?> updateStatus(
        @PathVariable Long id,
        @RequestBody StatusUpdateDTO dto  // 👈 read from JSON body
) {
    Ewaste updated = ewasteService.updateStatus(id, dto.getStatus());
    return ResponseEntity.ok(updated);
}

}