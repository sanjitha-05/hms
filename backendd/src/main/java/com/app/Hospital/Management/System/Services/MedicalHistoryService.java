package com.app.Hospital.Management.System.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Hospital.Management.System.entities.MedicalHistory;
import com.app.Hospital.Management.System.repositories.MedicalHistoryRepository;

@Service
public class MedicalHistoryService {
	@Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    public MedicalHistory addMedicalHistory(MedicalHistory medicalHistory) {
        
        return medicalHistoryRepository.save(medicalHistory);
        
    }

    public List<MedicalHistory> viewMedicalHistory(Long patientId) {
        return medicalHistoryRepository.findByPatientId(patientId);
    }
    
    public List<MedicalHistory> viewByTreatment(Long patient,String diagnosis){
    	List<MedicalHistory> h=new ArrayList<>();
    	List<MedicalHistory> medi=new ArrayList<>();
    	h=medicalHistoryRepository.findByPatientId(patient);
    	for(MedicalHistory m:h) {
    		if(m.getDiagnosis().equals(diagnosis)) {
    			medi.add(m);
    		}
    	}
    	return medi;
    }
    
    public void deleteMedicalHistory(Long patientId) {
    	List<MedicalHistory> medicalHistories = medicalHistoryRepository.findByPatientId(patientId);
        if (medicalHistories != null && !medicalHistories.isEmpty()) {
            medicalHistoryRepository.deleteAll(medicalHistories);
        } 
    }

}
