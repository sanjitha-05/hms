package com.app.Hospital.Management.System.Controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Hospital.Management.System.Services.AppointmentService;
import com.app.Hospital.Management.System.Services.DoctorScheduleService;
import com.app.Hospital.Management.System.entities.Appointment;
import com.app.Hospital.Management.System.entities.AppointmentStatus;
import com.app.Hospital.Management.System.exceptions.BadRequestException;
import com.app.Hospital.Management.System.exceptions.ConflictException;
import com.app.Hospital.Management.System.exceptions.ResourceNotFoundException;
import com.app.Hospital.Management.System.exceptions.ServiceUnavailableException;
import com.app.Hospital.Management.System.repositories.AppointmentRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hospital/appointments")
@Validated
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AppointmentRepository apprepo;
    @Autowired
    private DoctorScheduleService doctorService;
    
    
    @PostMapping("/book")
    public ResponseEntity<String> bookAppointment(@Valid @RequestBody Appointment appointment) {
    	try {
            String result = appointmentService.bookAppointment(appointment);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	
            throw new ServiceUnavailableException("Service is temporarily unavailable. Please try again later.");
        }
    }

    @GetMapping("/findall")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Optional<Appointment> optional = appointmentService.getAppointmentById(id);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        } else {
            throw new ResourceNotFoundException("Appointment not found with id: " + id);
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatientId(patientId);
        return ResponseEntity.ok(appointments);
    }
    
    @GetMapping("/getbydoctor/{id}")
    public ResponseEntity<List<Appointment>> getAppointmentByDoctorId(@PathVariable Long id) {
        List<Appointment> appointments = apprepo.findAppointmentByDoctorId(id);
        if (!appointments.isEmpty()) {
            return ResponseEntity.ok(appointments);
        } else {
            throw new ResourceNotFoundException("No appointments found for doctor ID: " + id);
        }
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Failed to delete appointment: " + e.getMessage());
        }
    }

    @PatchMapping("/put/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(@PathVariable Long id, @RequestBody String newStatus) {
        try {

            AppointmentStatus statusEnum = AppointmentStatus.valueOf(newStatus.toUpperCase());
            Appointment updatedAppointment = appointmentService.updateAppointmentStatus(id, statusEnum);
            return ResponseEntity.ok(updatedAppointment);
        } catch (Exception e) {
            throw new BadRequestException("Failed to update appointment status: " + e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(@PathVariable String status) {
        List<Appointment> appointments = appointmentService.getAppointmentsByStatus(status);
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found with status: " + status);
        }
        return ResponseEntity.ok(appointments);
    }
    
    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId) {
    	try {
            String response = appointmentService.cancelAppointment(appointmentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ConflictException("Failed to cancel appointment: " + e.getMessage());
        }
    }
    
    @PutMapping("/{appointmentId}/reschedule")
    public ResponseEntity<String> rescheduleAppointment(
            @PathVariable Long appointmentId,
            @RequestBody String newDateTimeString) {
    	 try {
             newDateTimeString = newDateTimeString.trim();
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
             LocalDateTime newDateTime = LocalDateTime.parse(newDateTimeString, formatter);
             LocalDate newDate = newDateTime.toLocalDate();
             LocalTime newTime = newDateTime.toLocalTime();   
             String response = appointmentService.rescheduleAppointment(appointmentId, newDate, newTime);
             return ResponseEntity.ok(response);
         } catch (Exception e) {
             throw new BadRequestException("Failed to reschedule appointment: " + e.getMessage());
         }

    }

}