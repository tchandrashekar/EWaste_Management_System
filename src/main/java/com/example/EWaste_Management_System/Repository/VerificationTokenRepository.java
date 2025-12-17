
package com.example.EWaste_Management_System.Repository;

import com.example.EWaste_Management_System.Entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}