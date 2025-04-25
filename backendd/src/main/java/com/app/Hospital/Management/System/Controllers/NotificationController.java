package com.app.Hospital.Management.System.Controllers;

import com.app.Hospital.Management.System.Services.NotificationService;
import com.app.Hospital.Management.System.entities.Notification;
import com.app.Hospital.Management.System.exceptions.BadRequestException;
import com.app.Hospital.Management.System.exceptions.ResourceNotFoundException;
import com.app.Hospital.Management.System.exceptions.ServiceUnavailableException;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hospital/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

     @PostMapping("/create/patient/{appointmentID}")
    public ResponseEntity<String> createPatientNotification(@PathVariable Long appointmentID) {
        try {
            service.createPatientNotification(appointmentID);
            return new ResponseEntity<>("Patient notification created successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ServiceUnavailableException("Failed to create patient notification: " + e.getMessage());
        }
    }
    @PostMapping("/create/doctor/{appointmentID}")
    public ResponseEntity<String> createDoctorNotification(@PathVariable Long appointmentID) {
        try {
            service.createDoctorNotification(appointmentID);
            return new ResponseEntity<>("Doctor notification created successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ServiceUnavailableException("Failed to create doctor notification: " + e.getMessage());
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Notification>> getNotificationsByPatientId(@PathVariable Long patientId) {
        List<Notification> notifications = service.getNotificationsByPatientId(patientId);
        if (notifications.isEmpty()) {
            throw new ResourceNotFoundException("No notifications found for patient ID: " + patientId);
        }
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Notification>> getNotificationsByDoctorId(@PathVariable Long doctorId) {
        List<Notification> notifications = service.getNotificationsByDoctorId(doctorId);
        if (notifications.isEmpty()) {
            throw new ResourceNotFoundException("No notifications found for doctor ID: " + doctorId);
        }
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
    	List<Notification> notifications = service.getAllNotifications();
        if (notifications.isEmpty()) {
            throw new ResourceNotFoundException("No notifications found");
        }
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Notification>> getNotificationById(@PathVariable Long id) {
        Optional<Notification> notification = service.getNotificationById(id);
        if (notification.isPresent()) {
            return new ResponseEntity<>(notification, HttpStatus.OK);
        } else {
        	 throw new ResourceNotFoundException("Notification not found with ID: " + id);        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotificationById(@PathVariable Long id) {
        try {
           service.deleteNotificationById(id);
            return new ResponseEntity<>("Notification deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
        	throw new ResourceNotFoundException("Failed to delete notification: " + e.getMessage());        }
    }
    
    
    @PostMapping("/generate-reminders")
    public ResponseEntity<String> generateReminders() {
    	try {
            service.generateRemindersForAppointments();
            return new ResponseEntity<>("Reminders generated successfully for tomorrow's appointments.", HttpStatus.OK);
        } catch (Exception e) {
            throw new ServiceUnavailableException("Failed to generate reminders: " + e.getMessage());
        }
    }
}