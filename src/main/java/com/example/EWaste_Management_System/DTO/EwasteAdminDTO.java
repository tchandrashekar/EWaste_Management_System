
package com.example.EWaste_Management_System.DTO;


import lombok.Data;

@Data
public class EwasteAdminDTO {

    private Long id;
    private String deviceType;
    private String brand;
    private String model;
    private String condition;
    private int quantity;
    private String pickupAddress;
    private String remarks;
    private String status;

    private String userName;
    private String userEmail;

    private String imageBase64;
}