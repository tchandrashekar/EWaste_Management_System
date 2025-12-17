
package com.example.EWaste_Management_System.Controller;

import com.example.EWaste_Management_System.DTO.EwasteDTO;
import com.example.EWaste_Management_System.DTO.PickupScheduleDTO;
import com.example.EWaste_Management_System.DTO.StatusUpdateDTO;
import com.example.EWaste_Management_System.Entity.Ewaste;
import com.example.EWaste_Management_System.Service.EwasteService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/ewaste")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminEwasteController {

    private final EwasteService ewasteService;

    @GetMapping("/all")
    public List<Ewaste> getAll() {
        return ewasteService.getAll();
    }

    @GetMapping("/user/{userId}")
    public List<Ewaste> getUserEwaste(@PathVariable Long userId) {
        return ewasteService.getUserEwaste(userId);
    }

    @PostMapping("/{userId}")
    public Ewaste createEwaste(@PathVariable Long userId, @RequestBody EwasteDTO dto) {
        return ewasteService.createEwaste(userId, dto);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody StatusUpdateDTO dto) {
        Ewaste updated = ewasteService.updateStatus(id, dto.getStatus());
        return ResponseEntity.ok(updated);
    }
/*
    @PutMapping("/schedule/{id}")
    public ResponseEntity<?> schedulePickup(
            @PathVariable Long id,
            @RequestParam("pickupDate") String pickupDateIso, // e.g. "2025-12-10T10:00"
            @RequestParam("timeSlot") String timeSlot,
            @RequestParam("staff") String staff
    ) {
        LocalDateTime dt = LocalDateTime.parse(pickupDateIso);
        Ewaste scheduled = ewasteService.schedulePickup(id, dt, timeSlot, staff);
        return ResponseEntity.ok(scheduled);
    }
  */
    @PutMapping("/schedule/{id}")
    public ResponseEntity<?> schedulePickup(
            @PathVariable Long id,
            @RequestBody PickupScheduleDTO dto
    ) {
        Ewaste scheduled = ewasteService.schedulePickup(
            id,
            dto.getPickupDate(),
            dto.getTimeSlot(),
            dto.getStaff()
    );
        return ResponseEntity.ok(scheduled);
    }

   
}