
package com.example.EWaste_Management_System.Controller;

import com.example.EWaste_Management_System.DTO.PickupEwasteDTO;
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
public List<PickupEwasteDTO> myPickups(Authentication auth) {
    return ewasteService.getMyPickups(auth.getName());
}


    @PutMapping("/picked/{id}")
    public Ewaste markPicked(@PathVariable Long id,
                             Authentication auth) {
        return ewasteService.markAsPicked(id, auth.getName());
    }
}
