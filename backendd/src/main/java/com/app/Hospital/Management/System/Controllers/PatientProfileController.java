package com.app.Hospital.Management.System.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Hospital.Management.System.Services.MedicalHistoryService;
import com.app.Hospital.Management.System.Services.PatientProfileService;
import com.app.Hospital.Management.System.entities.MedicalHistory;
import com.app.Hospital.Management.System.entities.PatientProfile;
import com.app.Hospital.Management.System.exceptions.IdNotFoundException;
import com.app.Hospital.Management.System.exceptions.UnauthorizedAccessException;
import com.app.Hospital.Management.System.repositories.MedicalHistoryRepository;
import com.app.Hospital.Management.System.repositories.PatientProfileRepository;
import com.app.Hospital.Management.System.repositories.UserRepository;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/hospital/patients")
public class PatientProfileController {
	@Autowired
	private PatientProfileService patientProfileService;
	@Autowired
	private MedicalHistoryService medicalHistoryService;
	@Autowired
	private PatientProfileRepository patientrepo;
	
	@Autowired
	private UserRepository userrepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	 
	@PostMapping("/save")
	public ResponseEntity<PatientProfile> savePatient(@Valid @RequestBody PatientProfile p){
		 
		PatientProfile savedPatient=patientProfileService.savePatient(p);
		return ResponseEntity.ok(savedPatient);
	}
	
	
	@GetMapping("/findall")
	public ResponseEntity<List<PatientProfile>> getAllPatients(){
		List<PatientProfile> patients=patientProfileService.getAllPatients();
		if (patients.isEmpty()) {
            throw new IdNotFoundException("No patients found");
        }
		return ResponseEntity.ok(patients);
	
	}
	
	@GetMapping("/get/{id}")
    public ResponseEntity<PatientProfile> getPatientById(@PathVariable Long id, Authentication authentication ) {

		if (authentication == null) {
			// Bypass authentication checks and directly fetch the patient
			Optional<PatientProfile> patient = patientProfileService.getPatientById(id);
			if (patient.isPresent()) {
				return ResponseEntity.ok(patient.get());
			} else {
				throw new IdNotFoundException("Patient not found with ID: " + id);
			}
		}
		String mail1=authentication.getName() ;
		Optional<PatientProfile> patient = patientProfileService.getPatientById(id);
        if (patient != null) {
        	PatientProfile p=patient.get();
        	Long id2=userrepo.findByEmail(mail1).get().getId();
        	if(id==id2)
        		return ResponseEntity.ok(p);
        	else
        		throw new UnauthorizedAccessException("Access denied! Please check your credentials and Patient ID");
        
        }
            throw new IdNotFoundException("Patient not found with ID: " + id);
        
	}
	@PutMapping("/put/{patientId}")
	 public PatientProfile updatePatient(@PathVariable Long patientId, @RequestBody PatientProfile p) {
		System.out.println(patientId);
        Optional<PatientProfile> existingPatient = patientrepo.findById(patientId); // Use the path variable id
        if (existingPatient.isPresent()) {
            PatientProfile patientProfile = existingPatient.get();
            if (p.getName() != null) {
                patientProfile.setName(p.getName());
            }
            if (p.getContactDetails() != null) {
                patientProfile.setContactDetails(p.getContactDetails());
            }
            if (p.getDateOfBirth() != null) {
                patientProfile.setDateOfBirth(p.getDateOfBirth());
            }
            // Do NOT update email here

            List<MedicalHistory> medicalHistory = medicalHistoryService.viewMedicalHistory(patientId);
            patientProfile.setMedicalHistory(medicalHistory);
            return patientrepo.save(patientProfile);
        } else {
            throw new RuntimeException("Patient not found with ID: " + patientId); // Use the correct ID in the exception
        }
    }
	
	// @PutMapping("/put/{id}")
	// public ResponseEntity<PatientProfile> updatePatient(@PathVariable Long id, @RequestBody PatientProfile p, Authentication authentication) {
	// 	String mail1=authentication.getName() ;
	// 	Long id2=userrepo.findByEmail(mail1).get().getId();
	// 	if(id!=id2)
	// 		throw new UnauthorizedAccessException("Access denied! Please check your credentials and Patient ID");
		
	// 	if (!patientProfileService.getPatientById(id).isPresent()) {
    //         throw new IdNotFoundException("Patient not found with ID: " + id);
    //     }
	// 	p.setPatientId(id); // Ensure the ID is set correctly
	//     PatientProfile updatedPatient = patientProfileService.updatePatient(id,p);
	//     return ResponseEntity.ok(updatedPatient);
	// }
	@DeleteMapping("/del/{id}")
	public ResponseEntity<Void> deletePatient(@PathVariable Long id,Authentication authentication){
		
		String mail1=authentication.getName() ;
		Long id2=userrepo.findByEmail(mail1).get().getId();
		if(id!=id2)
			throw new UnauthorizedAccessException("Access denied! Please check your credentials and Patient ID");
		 if (!patientProfileService.getPatientById(id).isPresent()) {
	            throw new IdNotFoundException("Patient not found with ID: " + id);
	        }
		patientProfileService.deletePatient(id);
		return ResponseEntity.noContent().build();
	}
    
}
