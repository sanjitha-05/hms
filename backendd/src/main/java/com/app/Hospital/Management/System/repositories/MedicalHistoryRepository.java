package com.app.Hospital.Management.System.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.Hospital.Management.System.entities.MedicalHistory;
@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
	@Query("select m from MedicalHistory m where m.patient IN (select p from PatientProfile p where p.patientId= :patientId)")
    List<MedicalHistory> findByPatientId(@Param("patientId") Long patientID);

}
