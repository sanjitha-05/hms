package com.app.Hospital.Management.System.Controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.app.Hospital.Management.System.Services.MedicalHistoryService;
import com.app.Hospital.Management.System.entities.MedicalHistory;
import com.app.Hospital.Management.System.exceptions.BadRequestException;
import com.app.Hospital.Management.System.exceptions.ResourceNotFoundException;
import com.app.Hospital.Management.System.exceptions.ServiceUnavailableException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hospital/history")
@Validated
public class MedicalHistoryController {
	
	@Autowired
    private MedicalHistoryService medicalHistoryService;

	@PostMapping("/save")
	public ResponseEntity<MedicalHistory> addMedicalHistory(@Valid @RequestBody MedicalHistory medicalHistory) {
		  try {
	            MedicalHistory newHistory = medicalHistoryService.addMedicalHistory(medicalHistory);
	            return ResponseEntity.ok(newHistory);
	        } catch (Exception e) {
	            throw new ServiceUnavailableException("Service is temporarily unavailable. Please try again later.");
	        }
	}
    
    @GetMapping("/view/{patientID}")
    public ResponseEntity<List<MedicalHistory>> viewMedicalHistory(@PathVariable("patientID") Long patientID) {
        if (patientID == null) {
            throw new BadRequestException("Patient ID cannot be null");
        }
        System.out.println(patientID);
        List<MedicalHistory> history = medicalHistoryService.viewMedicalHistory(patientID);
        if (history == null || history.isEmpty()) {
            throw new ResourceNotFoundException("No medical history found for patient ID: " + patientID);
        }
        return ResponseEntity.ok(history);
    }
    @GetMapping("/view/{patientID}/{diagnosis}")
    public ResponseEntity<List<MedicalHistory>> viewByTreatment(@PathVariable("patientID") Long patientID,@PathVariable("diagnosis") String diagnosis) {
        if (patientID == null) {
            throw new BadRequestException("Patient ID cannot be null");
        }
        else if(diagnosis==null) {
        	throw new BadRequestException("Diagnosis status cannot be null");
        }
        List<MedicalHistory> history = medicalHistoryService.viewByTreatment(patientID,diagnosis);
        if (history == null || history.isEmpty()) {
            throw new ResourceNotFoundException("No medical history found for patient ID: " + patientID);
        }
        return ResponseEntity.ok(history);
    }
    
    @DeleteMapping("/delete/{patientId}")
    public ResponseEntity<Void> deleteMedicalHistory(@PathVariable Long patientId) {
    	 try {
             medicalHistoryService.deleteMedicalHistory(patientId);
             return ResponseEntity.noContent().build();
         } catch (Exception e) {
             throw new ResourceNotFoundException("Failed to delete medical history for patient ID: " + patientId);
         }
    }

}
