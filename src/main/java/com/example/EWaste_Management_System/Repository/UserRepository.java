
package com.example.EWaste_Management_System.Repository;

import com.example.EWaste_Management_System.Model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User,Long>{
    Optional<User> findByEmail(String email);
    
}
