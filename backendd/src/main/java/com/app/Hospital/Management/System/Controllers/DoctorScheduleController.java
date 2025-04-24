package com.app.Hospital.Management.System.Controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.app.Hospital.Management.System.Services.DoctorScheduleService;
import com.app.Hospital.Management.System.entities.DoctorSchedule;
import com.app.Hospital.Management.System.entities.PatientProfile;
import com.app.Hospital.Management.System.entities.TimeSlot;
import com.app.Hospital.Management.System.exceptions.ConflictException;
import com.app.Hospital.Management.System.exceptions.IdNotFoundException;
import com.app.Hospital.Management.System.exceptions.ServiceUnavailableException;
import com.app.Hospital.Management.System.exceptions.UnauthorizedAccessException;
import com.app.Hospital.Management.System.repositories.UserRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/hospital/doctors")
public class DoctorScheduleController {
	
	@Autowired
	private DoctorScheduleService doctorScheduleService;
	@Autowired
	private UserRepository userrepo;
	
	@PostMapping("/save")
	@Transactional
	public ResponseEntity<DoctorSchedule> saveDoctor(@RequestBody DoctorSchedule d){
		 try {
	            DoctorSchedule doc = doctorScheduleService.saveDoctor(d);
	            return ResponseEntity.ok(doc);
	        } catch (Exception e) {
	            throw new ServiceUnavailableException("Service is temporarily unavailable. Please try again later.");
	        }
	}
	
	@GetMapping("/getall")
	public ResponseEntity<List<DoctorSchedule>> getAllDoctors(){
		List<DoctorSchedule> doctors=doctorScheduleService.getAllDoctors();
		 if (doctors.isEmpty()) {
	            throw new IdNotFoundException("No doctors found");
	        }
		return ResponseEntity.ok(doctors);
	}

// 	@GetMapping("/{doctorId}/name")
// public ResponseEntity<String> getDoctorName(@PathVariable Long doctorId) {
//     DoctorSchedule doctor = doctorScheduleService.getDoctorById(doctorId);
//     if (doctor!=null) {
//         return ResponseEntity.ok(doctor.getName());
//     } else {
//         throw new IdNotFoundException("Doctor with ID " + doctorId + " not found");
//     }
// }
	
	@GetMapping("{doctorId}/available-dates")
	public ResponseEntity<List<LocalDate>> getAvailableDates(@PathVariable Long doctorId) {
	    List<LocalDate> availableDates = doctorScheduleService.getAvailableDatesForDoctor(doctorId);
	    return ResponseEntity.ok(availableDates);
	}
	
	@GetMapping("/{doctorId}/available-dates/{date}/time-slots")
	public ResponseEntity<List<TimeSlot>> getAvailableTimeSlots(
	        @PathVariable Long doctorId,
	        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
	    List<TimeSlot> availableTimeSlots = doctorScheduleService.getAvailableTimeSlotsForDoctorAndDate(doctorId, date);
	    return ResponseEntity.ok(availableTimeSlots);
	}
	
	@PutMapping("/create/{id}")
	public ResponseEntity<String> createAvailability(@PathVariable Long id, Authentication authentication){
        try {
        	System.out.println("egduflkwdhfklh");
            String s = doctorScheduleService.createAvailability(id);
            return ResponseEntity.ok(s);
        } catch (Exception e) {
            throw new ConflictException("Conflict occurred while creating availability for doctor ID: " + id);
        }
    }	
}
