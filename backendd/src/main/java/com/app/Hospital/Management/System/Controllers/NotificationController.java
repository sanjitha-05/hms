package com.app.Hospital.Management.System.Controllers;

import com.app.Hospital.Management.System.Services.NotificationService;
import com.app.Hospital.Management.System.entities.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospital/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @PostMapping("/create/patient/{appointmentID}")
    public ResponseEntity<String> createPatientNotification(@PathVariable Long appointmentID) {
        service.createPatientNotification(appointmentID);
        return ResponseEntity.ok("Patient notification created successfully.");
    }

    @PostMapping("/create/doctor/{appointmentID}")
    public ResponseEntity<String> createDoctorNotification(@PathVariable Long appointmentID) {
        service.createDoctorNotification(appointmentID);
        return ResponseEntity.ok("Doctor notification created successfully.");
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Notification>> getNotificationsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(service.getNotificationsByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Notification>> getNotificationsByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity.ok(service.getNotificationsByDoctorId(doctorId));
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(service.getAllNotifications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getNotificationById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotificationById(@PathVariable Long id) {
        service.deleteNotificationById(id);
        return ResponseEntity.ok("Notification deleted successfully.");
    }

    @PostMapping("/generate-reminders")
    public ResponseEntity<String> generateReminders() {
        service.generateRemindersForAppointments();
        return ResponseEntity.ok("Reminders generated successfully for tomorrow's appointments.");
    }
}
