package com.app.Hospital.Management.System.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.app.Hospital.Management.System.Services.AppointmentService;
import com.app.Hospital.Management.System.entities.Appointment;
import com.app.Hospital.Management.System.entities.AppointmentStatus;
import com.app.Hospital.Management.System.exceptions.ResourceNotFoundException;
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

    @PostMapping("/book")
    public ResponseEntity<String> bookAppointment(@Valid @RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.bookAppointment(appointment));
    }

    @GetMapping("/findall")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId));
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
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/put/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(@PathVariable Long id, @RequestBody String newStatus) {
        AppointmentStatus statusEnum = AppointmentStatus.valueOf(newStatus.toUpperCase());
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(id, statusEnum));
    }

    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentId));
    }
}
