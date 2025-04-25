package com.app.Hospital.Management.System.Services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.Hospital.Management.System.entities.MedicalHistory;
import com.app.Hospital.Management.System.entities.PatientProfile;
import com.app.Hospital.Management.System.repositories.AppointmentRepository;
import com.app.Hospital.Management.System.repositories.PatientProfileRepository;

import jakarta.transaction.Transactional;

@Service
public class PatientProfileService {
    private static final Logger logger = LoggerFactory.getLogger(PatientProfileService.class);

    @Autowired
    private PatientProfileRepository patientRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private MedicalHistoryService medicalHistoryService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public PatientProfile savePatient(PatientProfile p) {
        logger.info("Saving new patient profile: {}", p.getName());
        return patientRepository.save(p);
    }

    public List<PatientProfile> getAllPatients() {
        logger.info("Fetching all patient profiles.");
        return patientRepository.findAll();
    }

    public Optional<PatientProfile> getPatientById(Long id) {
        logger.info("Fetching patient profile with ID: {}", id);
        return patientRepository.findById(id);
    }

    @Transactional
    public void deletePatient(Long id) {
        logger.warn("Deleting patient profile with ID: {}", id);
        appointmentRepository.deleteByPatientId(id);
        patientRepository.deleteById(id);
    }

    public PatientProfile updatePatient(Long patientId, PatientProfile p) {
        logger.info("Updating patient profile for ID: {}", patientId);
        Optional<PatientProfile> existingPatient = patientRepository.findById(patientId);

        if (existingPatient.isPresent()) {
            PatientProfile patientProfile = existingPatient.get();

            if (p.getName() != null) {
                patientProfile.setName(p.getName());
            }

            if (p.getContactDetails() != null) {
                patientProfile.setContactDetails(p.getContactDetails());
            }

            List<MedicalHistory> medicalHistory = medicalHistoryService.viewMedicalHistory(patientId);
            if (medicalHistory.isEmpty()) {
                logger.warn("No medical history found for Patient ID: {}", patientId);
            }
            patientProfile.setMedicalHistory(medicalHistory);

            logger.info("Successfully updated patient profile for ID: {}", patientId);
            return patientRepository.save(patientProfile);
        } else {
            logger.error("Patient not found with ID: {}", patientId);
            throw new RuntimeException("Patient not found");
        }
    }
}
