
package com.example.EWaste_Management_System.DTO;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EwasteDTO {
    private Long userId;
    private String deviceType;
    private String brand;
    private String model;
    private String condition;
    private int quantity;
    private String imageUrl;
    private String pickupAddress;
    private String remarks;
}