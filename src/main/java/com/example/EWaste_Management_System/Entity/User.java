
package com.example.EWaste_Management_System.Entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Data
public class User {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Column(unique=true)
    private String email;
    
    private String password;
    
    private boolean enabled=false;
    
    @ManyToMany(fetch=FetchType.EAGER)
    private Set<Role> roles=new HashSet<>();

 
}
