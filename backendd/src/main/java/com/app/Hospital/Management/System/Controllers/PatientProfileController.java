package com.app.Hospital.Management.System.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.app.Hospital.Management.System.Services.PatientProfileService;
import com.app.Hospital.Management.System.entities.PatientProfile;
import com.app.Hospital.Management.System.exceptions.IdNotFoundException;
import com.app.Hospital.Management.System.exceptions.UnauthorizedAccessException;
import com.app.Hospital.Management.System.repositories.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hospital/patients")
public class PatientProfileController {

    @Autowired
    private PatientProfileService patientProfileService;
    
    @Autowired
    private UserRepository userrepo;

    @PostMapping("/save")
    public ResponseEntity<PatientProfile> savePatient(@Valid @RequestBody PatientProfile p) {
        return ResponseEntity.ok(patientProfileService.savePatient(p));
    }

    @GetMapping("/findall")
    public ResponseEntity<List<PatientProfile>> getAllPatients() {
        return ResponseEntity.ok(patientProfileService.getAllPatients());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PatientProfile> getPatientById(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.ok(patientProfileService.getPatientById(id)
                    .orElseThrow(() -> new IdNotFoundException("Patient not found with ID: " + id)));
        }

        String email = authentication.getName();
        Long authenticatedUserId = userrepo.findByEmail(email).get().getId();

        if (!id.equals(authenticatedUserId)) {
            throw new UnauthorizedAccessException("Access denied! Please check your credentials and Patient ID.");
        }

        return ResponseEntity.ok(patientProfileService.getPatientById(id)
                .orElseThrow(() -> new IdNotFoundException("Patient not found with ID: " + id)));
    }

    @PutMapping("/put/{patientId}")
    public ResponseEntity<PatientProfile> updatePatient(@PathVariable Long patientId, @RequestBody PatientProfile p) {
        return ResponseEntity.ok(patientProfileService.updatePatient(patientId, p));
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        Long authenticatedUserId = userrepo.findByEmail(email).get().getId();

        if (!id.equals(authenticatedUserId)) {
            throw new UnauthorizedAccessException("Access denied! Please check your credentials and Patient ID.");
        }

        patientProfileService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
