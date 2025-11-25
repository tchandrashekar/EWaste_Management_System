
package com.example.EWaste_Management_System.Repository;

import com.example.EWaste_Management_System.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role,Long>{
    Role findByName(String name);
}
