package com.app.Hospital.Management.System.Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.Hospital.Management.System.entities.Appointment;
import com.app.Hospital.Management.System.entities.DoctorSchedule;
import com.app.Hospital.Management.System.entities.ScheduledId;
import com.app.Hospital.Management.System.entities.TimeSlot;
import com.app.Hospital.Management.System.exceptions.ResourceNotFoundException;
import com.app.Hospital.Management.System.repositories.DoctorScheduleRepository;

import jakarta.transaction.Transactional;
@Service
public class DoctorScheduleService {
	
	@Autowired
	private DoctorScheduleRepository repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public DoctorSchedule saveDoctor(DoctorSchedule doctor) {
		
        return repo.save(doctor);
    }
	
	
	public List<LocalDate> getAvailableDatesForDoctor(Long doctorId) {
        // Fetch all schedules for the doctor
        List<DoctorSchedule> schedules = repo.findByDoctorId(doctorId);

        // Filter schedules where there are available (non-blocked) time slots
        return schedules.stream()
                .filter(schedule -> schedule.getAvailableTimeSlots().stream().anyMatch(slot -> !slot.isBlocked()))
                .map(DoctorSchedule::getDate) // Extract the date
                .collect(Collectors.toList());
    }

	// public DoctorSchedule getDoctorById(Long doctorId) {
    //     return repo.findFirstById(doctorId);
    // }
	
	public List<TimeSlot> getAvailableTimeSlotsForDoctorAndDate(Long doctorId, LocalDate date) {
        DoctorSchedule doctorSchedule = repo.findByDoctorIdAndDate(doctorId, date)
                .orElseThrow(() -> new ResourceNotFoundException("No schedule found for the doctor on the given date"));

        return doctorSchedule.getAvailableTimeSlots().stream()
                .filter(timeSlot -> !timeSlot.isBlocked())
                .toList();
    }
	
	public List<DoctorSchedule> getAllDoctors(){
		return repo.findAll();
	}
	
	 public Optional<DoctorSchedule> getDoctorSchedule(Long doctorId, LocalDate date) {
	        return repo.findById(new ScheduledId(doctorId, date));
	  }
	 
	 public String createAvailability(Long doctorId) {
		 
		 String doctorName = repo.findNameByDoctorId(doctorId); // Use DoctorRepository or DoctorScheduleRepository

		    if (doctorName == null || doctorName.isEmpty()) {
		        throw new IllegalArgumentException("Doctor not found with ID: " + doctorId);
		    }
			
//		 String doctorName = repo.findById(doctorId)
//                 .orElseThrow(() -> new IllegalArgumentException("Doctor not found"))
//                 .getName();
//			List<DoctorSchedule> previousAvailabilities=repo.findByDoctorId(doctorId);
//			repo.deleteAll(previousAvailabilities);
			 
			LocalDate startDate=LocalDate.now();
			LocalDate endDate=startDate.plusDays(6);
			
			List<TimeSlot> timeSlots=new ArrayList<>();
			timeSlots.add(new TimeSlot(LocalTime.of(9, 0),false));
			timeSlots.add(new TimeSlot(LocalTime.of(10, 0),false));
			timeSlots.add(new TimeSlot(LocalTime.of(11, 0),false));
			timeSlots.add(new TimeSlot(LocalTime.of(13, 0),false));
			timeSlots.add(new TimeSlot(LocalTime.of(14, 0),false));
			timeSlots.add(new TimeSlot(LocalTime.of(15, 0),false));
			
			for(LocalDate date=startDate;!date.isAfter(endDate);date=date.plusDays(1)) {
				
				DoctorSchedule sc=new DoctorSchedule(
						doctorId,doctorName,date,new ArrayList<>(timeSlots));
				
				repo.save(sc);
			}
			return "Availability Created Successfully";
		}


	public Optional<List<Appointment>> getAppointmentByDoctorId(Long id) {
		// TODO Auto-generated method stub
		
		return null;
	}
	 
	 
}
