
package com.example.EWaste_Management_System.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Role {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    private String name ;//Admin Or User
    
}
