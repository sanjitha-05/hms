package com.app.Hospital.Management.System.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.app.Hospital.Management.System.Services.MedicalHistoryService;
import com.app.Hospital.Management.System.entities.MedicalHistory;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hospital/history")
@Validated
public class MedicalHistoryController {
    
    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @PostMapping("/save")
    public ResponseEntity<MedicalHistory> addMedicalHistory(@Valid @RequestBody MedicalHistory medicalHistory) {
        return ResponseEntity.ok(medicalHistoryService.addMedicalHistory(medicalHistory));
    }

    @GetMapping("/view/{patientID}")
    public ResponseEntity<List<MedicalHistory>> viewMedicalHistory(@PathVariable("patientID") Long patientID) {
        return ResponseEntity.ok(medicalHistoryService.viewMedicalHistory(patientID));
    }

    @GetMapping("/view/{patientID}/{diagnosis}")
    public ResponseEntity<List<MedicalHistory>> viewByTreatment(
            @PathVariable("patientID") Long patientID, 
            @PathVariable("diagnosis") String diagnosis) {
        return ResponseEntity.ok(medicalHistoryService.viewByTreatment(patientID, diagnosis));
    }

    @DeleteMapping("/delete/{patientId}")
    public ResponseEntity<Void> deleteMedicalHistory(@PathVariable Long patientId) {
        medicalHistoryService.deleteMedicalHistory(patientId);
        return ResponseEntity.noContent().build();
    }
}
