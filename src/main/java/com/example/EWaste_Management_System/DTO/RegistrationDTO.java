
package com.example.EWaste_Management_System.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationDTO {
    
    @NotBlank(message = "Name required")
    private String name;
    
    @NotBlank(message="Email required")
    @Email(message="Invalid email")
    private String email;
    
    @NotBlank(message="Password required")
    private String password;
    
    private String phone;
}
