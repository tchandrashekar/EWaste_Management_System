
package com.example.EWaste_Management_System.DTO;

import java.time.LocalDateTime;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EwasteDTO {
    private Long id;
    private String deviceType;
    private String brand;
    private String model;
    private String condition;
    private int quantity;
    private String imageUrl;
    private String pickupAddress;
    private String remarks;
    private String status; 
     private LocalDateTime pickupDate;
    private String timeSlot;
    private String assignedStaff;
      private String imageBase64; 
}
