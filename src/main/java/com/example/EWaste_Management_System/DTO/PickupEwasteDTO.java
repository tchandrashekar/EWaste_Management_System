
package com.example.EWaste_Management_System.DTO;


import java.time.LocalDateTime;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickupEwasteDTO {

    private Long id;
    private String deviceType;
    private String brand;
    private String model;
       private String condition;
    private Integer quantity;
  private String remarks;
    private String pickupAddress;
    private LocalDateTime pickupDate;
    private String pickupTimeSlot;
    private String assignedStaff;
    private String status;

    // 🔥 USER DETAILS (IMPORTANT)
    private String userName;
    private String userEmail;
    private String userPhone;

    // 🔥 IMAGE
    private String imageBase64;
}
