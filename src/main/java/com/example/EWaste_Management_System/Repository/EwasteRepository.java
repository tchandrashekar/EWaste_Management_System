
package com.example.EWaste_Management_System.Repository;


import com.example.EWaste_Management_System.Entity.Ewaste;
import com.example.EWaste_Management_System.Entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EwasteRepository extends JpaRepository<Ewaste, Long> {
    List<Ewaste> findByUser(User user);
     List<Ewaste> findByPickupPerson(User pickupPerson);
}