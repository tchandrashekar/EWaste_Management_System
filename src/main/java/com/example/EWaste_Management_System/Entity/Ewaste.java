/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.EWaste_Management_System.Entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ewaste {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceType;
    private String brand;
    private String model;

    @Column(name = "`condition`")
    private String condition;

    private int quantity;
    private String imageUrl;
    private String pickupAddress;
    private String remarks;

    @Enumerated(EnumType.STRING)
    private Status status = Status.SUBMITTED;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Scheduling fields
    private LocalDateTime pickupDate;
    private String pickupTimeSlot;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
