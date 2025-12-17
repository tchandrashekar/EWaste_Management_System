
package com.example.EWaste_Management_System.DTO;


import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PickupScheduleDTO {
    private LocalDateTime pickupDate;  // ISO format String
    private String timeSlot;
    private String staff;
}