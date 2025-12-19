
package com.example.EWaste_Management_System.Controller;

import com.example.EWaste_Management_System.Entity.Ewaste;
import com.example.EWaste_Management_System.Service.EwasteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pickup/ewaste")
@PreAuthorize("hasRole('PICKUP')")
@RequiredArgsConstructor
public class PickupEwasteController {

    private final EwasteService ewasteService;

    @GetMapping("/my-pickups")
    public List<Ewaste> myPickups(Authentication auth) {
        return ewasteService.getPickupsForPerson(auth.getName());
    }

    @PutMapping("/picked/{id}")
    public Ewaste markPicked(@PathVariable Long id,
                             Authentication auth) {
        return ewasteService.markAsPicked(id, auth.getName());
    }
}
