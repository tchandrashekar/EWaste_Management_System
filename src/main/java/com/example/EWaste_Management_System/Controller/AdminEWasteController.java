
package com.example.EWaste_Management_System.Controller;

import com.example.EWaste_Management_System.Entity.Ewaste;
import com.example.EWaste_Management_System.Entity.Status;
import com.example.EWaste_Management_System.Repository.EwasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/ewaste")
@PreAuthorize("hasRole('ADMIN')")
public class AdminEWasteController {
    
    @Autowired
    private EwasteRepository ewasteRepository;

    @PutMapping("/update-status/{id}/{status}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @PathVariable Status status
    ) {
        Ewaste ewaste = ewasteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        ewaste.setStatus(status);
        ewasteRepository.save(ewaste);

        return ResponseEntity.ok("Status updated to " + status);
    }
}
