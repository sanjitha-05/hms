package com.app.Hospital.Management.System.Services;

import java.util.List;
import java.util.Optional;

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
	@Autowired
	private PatientProfileRepository patientRepository;
	@Autowired 
	private AppointmentRepository appointmentRepository;
	
	@Autowired
    private MedicalHistoryService medicalHistoryService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	public PatientProfile savePatient(PatientProfile p) {
		//p.setPassword(passwordEncoder.encode(p.getPassword()));
		return patientRepository.save(p);
	}


	public List<PatientProfile> getAllPatients() {
		// TODO Auto-generated method stub
		return patientRepository.findAll();
	}

	public Optional<PatientProfile> getPatientById(Long id) {
		// TODO Auto-generated method stub
		return patientRepository.findById(id);
	}
	@Transactional
	public void deletePatient(Long id) {
		appointmentRepository.deleteByPatientId(id);
		 patientRepository.deleteById(id);
	}
	
	
	
	public PatientProfile updatePatient(Long patientId,PatientProfile p) {
        Optional<PatientProfile> existingPatient = patientRepository.findById(p.getPatientId());
        if (existingPatient.isPresent()) {
            PatientProfile patientProfile = existingPatient.get();
            if(p.getName()!=null)
            patientProfile.setName(p.getName());
            if(p.getContactDetails()!=null)
            patientProfile.setContactDetails(p.getContactDetails());
            // Update medical history if provided
            System.out.println("hjjegfdkhfsil.ksahgdk.hsdg");
            List<MedicalHistory> medicalHistory = medicalHistoryService.viewMedicalHistory(patientId);
            System.out.println(medicalHistory);
            patientProfile.setMedicalHistory(medicalHistory);
            return patientRepository.save(patientProfile);
        }  else {
            throw new RuntimeException("Patient not found");
        }
    }

	
	
	
	
}
