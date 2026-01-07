
package com.example.EWaste_Management_System.Service;

import com.example.EWaste_Management_System.DTO.EwasteAdminDTO;
import com.example.EWaste_Management_System.DTO.EwasteDTO;
import com.example.EWaste_Management_System.DTO.PickupEwasteDTO;
import com.example.EWaste_Management_System.Entity.Ewaste;
import com.example.EWaste_Management_System.Entity.Status;
import com.example.EWaste_Management_System.Entity.User;
import com.example.EWaste_Management_System.Repository.EwasteRepository;
import com.example.EWaste_Management_System.Repository.UserRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class EwasteService {

    
    private final UserRepository userRepository;
    private final EwasteRepository ewasteRepository;
    private final EmailService emailService;

     public Ewaste createRequestWithImage(String email, String deviceType, String brand, String model,
                                         String condition, int quantity, String pickupAddress,
                                         String remarks, MultipartFile imageFile) throws IOException {

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
        ew.setStatus(Status.SUBMITTED);
        if (imageFile != null && !imageFile.isEmpty()) {
            ew.setImageData(imageFile.getBytes());
            ew.setImageUrl(imageFile.getOriginalFilename());
        }

        return ewasteRepository.save(ew);
    }
     
     
public List<EwasteDTO> getMyRequestsWithImages(String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return ewasteRepository.findByUser(user)
            .stream()
            .map(ew -> {
                EwasteDTO dto = new EwasteDTO();
                dto.setId(ew.getId());
                dto.setDeviceType(ew.getDeviceType());
                dto.setBrand(ew.getBrand());
                dto.setModel(ew.getModel());
                dto.setCondition(ew.getCondition());
                dto.setQuantity(ew.getQuantity());
                dto.setPickupAddress(ew.getPickupAddress());
                dto.setRemarks(ew.getRemarks());
                 if (ew.getStatus() != null) {
                    dto.setStatus(ew.getStatus().name());
                }
                 
                 dto.setPickupDate(ew.getPickupDate());
                 dto.setTimeSlot(ew.getPickupTimeSlot());
                 dto.setAssignedStaff(ew.getAssignedStaff());
                 
                

                if (ew.getImageData() != null) {
                    dto.setImageBase64(
                        Base64.getEncoder().encodeToString(ew.getImageData())
                    );
                }

                return dto;
            })
            .collect(Collectors.toList());
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

    
    public List<EwasteAdminDTO> getAllAdminRequests() {

    return ewasteRepository.findAll()
            .stream()
            .map(ew -> {
                EwasteAdminDTO dto = new EwasteAdminDTO();

                dto.setId(ew.getId());
                dto.setDeviceType(ew.getDeviceType());
                dto.setBrand(ew.getBrand());
                dto.setModel(ew.getModel());
                dto.setCondition(ew.getCondition());
                dto.setQuantity(ew.getQuantity());
                dto.setPickupAddress(ew.getPickupAddress());
                dto.setRemarks(ew.getRemarks());
                dto.setStatus(ew.getStatus().name());

                if (ew.getUser() != null) {
                    dto.setUserName(ew.getUser().getName());
                    dto.setUserEmail(ew.getUser().getEmail());
                }

                if (ew.getImageData() != null) {
                    dto.setImageBase64(
                        Base64.getEncoder().encodeToString(ew.getImageData())
                    );
                }

                return dto;
            })
            .toList();
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
    public Ewaste schedulePickup(Long id,Long pickupPersonId, LocalDateTime pickupDate, String timeSlot, String assignedStaff) {
        Ewaste ewaste = ewasteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
         User pickupPerson = userRepository.findById(pickupPersonId)
            .orElseThrow(() -> new RuntimeException("Pickup person not found"));
         ewaste.setPickupPerson(pickupPerson);
        ewaste.setPickupDate(pickupDate);
        ewaste.setPickupTimeSlot(timeSlot);
        ewaste.setAssignedStaff(assignedStaff);
        ewaste.setStatus(Status.SCHEDULED);
        Ewaste saved = ewasteRepository.save(ewaste);

        // notify user via email
        String to = saved.getUser().getEmail();
        String body = "Hi " + saved.getUser().getName() + ",\n\nYour e-waste pickup is scheduled on " +
                pickupDate.toString() + " Time slot: " + timeSlot + ".\nAssigned staff: " + assignedStaff;
        emailService.sendEmail(to, "E-Waste Pickup Scheduled", body);

        return saved;
    }
    
    public List<Ewaste> getPickupsForPerson(String email) {
    User pickupPerson = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return ewasteRepository.findByPickupPerson(pickupPerson);
}

    public Ewaste markAsPicked(Long ewasteId, String email) {

    Ewaste ewaste = ewasteRepository.findById(ewasteId)
            .orElseThrow(() -> new RuntimeException("Request not found"));

    if (ewaste.getPickupPerson() == null ||
        !ewaste.getPickupPerson().getEmail().equals(email)) {
        throw new RuntimeException("Not assigned to you");
    }

    if (ewaste.getStatus() != Status.SCHEDULED) {
        throw new RuntimeException("Pickup not scheduled");
    }

    ewaste.setStatus(Status.PICKED);
    return ewasteRepository.save(ewaste);
}

    public Ewaste completeRequest(Long ewasteId) {

    Ewaste ewaste = ewasteRepository.findById(ewasteId)
            .orElseThrow(() -> new RuntimeException("Request not found"));

    if (ewaste.getStatus() != Status.PICKED) {
        throw new RuntimeException("Pickup not completed yet");
    }

    ewaste.setStatus(Status.COMPLETED);
    return ewasteRepository.save(ewaste);
}
    
      public List<PickupEwasteDTO> getMyPickups(String email) {

        User pickupPerson = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Pickup person not found"));

        return ewasteRepository.findByPickupPerson(pickupPerson)
                .stream()
                .map(ew -> {

                    PickupEwasteDTO dto = new PickupEwasteDTO();

                    // Ewaste details
                    dto.setId(ew.getId());
                    dto.setDeviceType(ew.getDeviceType());
                    dto.setBrand(ew.getBrand());
                    dto.setModel(ew.getModel());
                    dto.setCondition(ew.getCondition());
                    dto.setQuantity(ew.getQuantity());
                    dto.setPickupAddress(ew.getPickupAddress());
                    dto.setRemarks(ew.getRemarks());
                    dto.setStatus(ew.getStatus().name());

                    // Pickup scheduling details
                    dto.setPickupDate(ew.getPickupDate());
                    dto.setPickupTimeSlot(ew.getPickupTimeSlot());
                    dto.setAssignedStaff(ew.getAssignedStaff());

                    // User details
                    if (ew.getUser() != null) {
                        dto.setUserName(ew.getUser().getName());
                        dto.setUserEmail(ew.getUser().getEmail());
                        dto.setUserPhone(ew.getUser().getPhone());
                    }

                    // Image
                    if (ew.getImageData() != null) {
                        dto.setImageBase64(
                                Base64.getEncoder().encodeToString(ew.getImageData())
                        );
                    }

                    return dto;
                })
                .toList();
    }


}