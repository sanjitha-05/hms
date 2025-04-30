package com.app.Hospital.Management.System.Controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.app.Hospital.Management.System.Services.DoctorScheduleService;
import com.app.Hospital.Management.System.entities.DoctorSchedule;
import com.app.Hospital.Management.System.entities.TimeSlot;

@RestController
@RequestMapping("/api/hospital/doctors")
public class DoctorScheduleController {
    
    @Autowired
    private DoctorScheduleService doctorScheduleService;

    @PostMapping("/save")
    public ResponseEntity<DoctorSchedule> saveDoctor(@RequestBody DoctorSchedule doctor) {
        return ResponseEntity.ok(doctorScheduleService.saveDoctor(doctor));
    }

    @GetMapping("/getall")
    public ResponseEntity<List<DoctorSchedule>> getAllDoctors() {
        return ResponseEntity.ok(doctorScheduleService.getAllDoctors());
    }

    @GetMapping("/{doctorId}/available-dates")
    public ResponseEntity<List<LocalDate>> getAvailableDates(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorScheduleService.getAvailableDatesForDoctor(doctorId));
    }

    @GetMapping("/{doctorId}/available-dates/{date}/time-slots")
    public ResponseEntity<List<TimeSlot>> getAvailableTimeSlots(
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(doctorScheduleService.getAvailableTimeSlotsForDoctorAndDate(doctorId, date));
    }

    @PutMapping("/create/{id}")
    public ResponseEntity<String> createAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(doctorScheduleService.createAvailability(id));
    }
    
    @GetMapping("/{doctorId}/free-dates")
    public ResponseEntity<List<LocalDate>> getFreeDates(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorScheduleService.getFreeDatesWithNoAppointments(doctorId));
    }
    
    @GetMapping("/{doctorId}/blocked-dates")
    public ResponseEntity<List<LocalDate>> getBlockedDates(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorScheduleService.getBlockedDates(doctorId));
    }

    @PutMapping("/unblock/{doctorID}/{date}")
    public ResponseEntity<String> unblockAvailability(@PathVariable Long doctorID, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(doctorScheduleService.unblockAvailability(doctorID, date));
    }

    @PostMapping("/{doctorId}/block-date")
    public ResponseEntity<String> blockDate(
        @PathVariable Long doctorId,
        @RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    doctorScheduleService.blockAvailability(doctorId, date);
    return ResponseEntity.ok("Date blocked successfully.");
}
}
