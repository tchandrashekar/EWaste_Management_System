
package com.example.EWaste_Management_System.Entity;

import jakarta.persistence.*;
import java.util.Date;
import lombok.*;


@Entity
@Data
public class VerificationToken {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    private String token;
    
    @OneToOne
    private User user;
    
    private Date expiryDate;
}
