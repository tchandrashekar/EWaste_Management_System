
package com.example.EWaste_Management_System.Service;

import com.example.EWaste_Management_System.DTO.EwasteDTO;
import com.example.EWaste_Management_System.Entity.Ewaste;
import com.example.EWaste_Management_System.Entity.Status;
import com.example.EWaste_Management_System.Entity.User;
import com.example.EWaste_Management_System.Repository.EwasteRepository;
import com.example.EWaste_Management_System.Repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class EwasteService {

    private final UserRepository userRepository;
    private final EwasteRepository ewasteRepository;
    private final EmailService emailService;

    public Ewaste createRequest(String email, String deviceType, String brand, String model,
                                String condition, int quantity, String pickupAddress, String remarks) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ewaste ew = new Ewaste();
        ew.setDeviceType(deviceType);
        ew.setBrand(brand);
        ew.setModel(model);
        ew.setCondition(condition);
        ew.setQuantity(quantity);
        ew.setPickupAddress(pickupAddress);
        ew.setRemarks(remarks);
        ew.setUser(user);

        return ewasteRepository.save(ew);
    }

    public Ewaste createEwaste(Long userId, EwasteDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ewaste ew = new Ewaste();
        ew.setDeviceType(dto.getDeviceType());
        ew.setBrand(dto.getBrand());
        ew.setModel(dto.getModel());
        ew.setCondition(dto.getCondition());
        ew.setQuantity(dto.getQuantity());
        ew.setPickupAddress(dto.getPickupAddress());
        ew.setRemarks(dto.getRemarks());
        ew.setImageUrl(dto.getImageUrl());
        ew.setUser(user);

        return ewasteRepository.save(ew);
    }

    public List<Ewaste> getMyRequests(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ewasteRepository.findByUser(user);
    }

    public List<Ewaste> getUserEwaste(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ewasteRepository.findByUser(user);
    }

    public List<Ewaste> getAll() {
        return ewasteRepository.findAll();
    }

    @Transactional
    public Ewaste updateStatus(Long id, Status newStatus) {
        Ewaste ewaste = ewasteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        ewaste.setStatus(newStatus);
        return ewasteRepository.save(ewaste);
    }

    @Transactional
    public Ewaste schedulePickup(Long id, LocalDateTime pickupDate, String timeSlot, String assignedStaff) {
        Ewaste ewaste = ewasteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        ewaste.setPickupDate(pickupDate);
        ewaste.setPickupTimeSlot(timeSlot);
        ewaste.setStatus(Status.SCHEDULED);
        Ewaste saved = ewasteRepository.save(ewaste);

        // notify user via email
        String to = saved.getUser().getEmail();
        String body = "Hi " + saved.getUser().getName() + ",\n\nYour e-waste pickup is scheduled on " +
                pickupDate.toString() + " Time slot: " + timeSlot + ".\nAssigned staff: " + assignedStaff;
        emailService.sendEmail(to, "E-Waste Pickup Scheduled", body);

        return saved;
    }
}