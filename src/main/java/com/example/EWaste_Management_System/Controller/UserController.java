
package com.example.EWaste_Management_System.Controller;

import com.example.EWaste_Management_System.Model.User;
import com.example.EWaste_Management_System.Service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }
    
    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id,@RequestBody User user){
        return userService.updateUser(id,user);
    }
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }
    
}
