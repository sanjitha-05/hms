package com.app.Hospital.Management.System.Services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Hospital.Management.System.entities.MedicalHistory;
import com.app.Hospital.Management.System.repositories.MedicalHistoryRepository;

@Service
public class MedicalHistoryService {
    private static final Logger logger = LoggerFactory.getLogger(MedicalHistoryService.class);

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    public MedicalHistory addMedicalHistory(MedicalHistory medicalHistory) {
    	System.out.println(medicalHistory);
        logger.info("Adding new medical history for Patient ID: {}", medicalHistory.getPatient().getPatientId());
        return medicalHistoryRepository.save(medicalHistory);
    }

    public List<MedicalHistory> viewMedicalHistory(Long patientId) {
        logger.info("Fetching medical history for Patient ID: {}", patientId);
        return medicalHistoryRepository.findByPatientId(patientId);
    }

    public List<MedicalHistory> viewByTreatment(Long patientId, String diagnosis) {
        logger.info("Fetching medical history for Patient ID: {} with diagnosis: {}", patientId, diagnosis);
        List<MedicalHistory> history = medicalHistoryRepository.findByPatientId(patientId);
        List<MedicalHistory> filteredHistory = new ArrayList<>();

        for (MedicalHistory m : history) {
            if (m.getDiagnosis().equalsIgnoreCase(diagnosis)) {
                filteredHistory.add(m);
            }
        }

        if (filteredHistory.isEmpty()) {
            logger.warn("No medical history found for Patient ID: {} with diagnosis: {}", patientId, diagnosis);
        }
        
        return filteredHistory;
    }

    public void deleteMedicalHistory(Long patientId) {
        logger.info("Attempting to delete medical history for Patient ID: {}", patientId);
        List<MedicalHistory> medicalHistories = medicalHistoryRepository.findByPatientId(patientId);

        if (medicalHistories != null && !medicalHistories.isEmpty()) {
            medicalHistoryRepository.deleteAll(medicalHistories);
            logger.info("Successfully deleted medical history for Patient ID: {}", patientId);
        } else {
            logger.warn("No medical history found to delete for Patient ID: {}", patientId);
        }
    }
}
