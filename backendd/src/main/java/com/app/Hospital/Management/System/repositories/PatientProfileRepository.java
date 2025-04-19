package com.app.Hospital.Management.System.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Hospital.Management.System.entities.PatientProfile;
@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {
	
}
