package com.app.Hospital.Management.System.Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.Hospital.Management.System.entities.*;
import com.app.Hospital.Management.System.exceptions.ResourceNotFoundException;
import com.app.Hospital.Management.System.repositories.*;

@Service
public class DoctorScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(DoctorScheduleService.class);

    @Autowired
    private DoctorScheduleRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public DoctorSchedule saveDoctor(DoctorSchedule doctor) {
        logger.info("Saving doctor schedule for Doctor ID: {}", doctor.getDoctorId());
        return repo.save(doctor);
    }

    public List<LocalDate> getAvailableDatesForDoctor(Long doctorId) {
        logger.info("Fetching available dates for Doctor ID: {}", doctorId);
        List<DoctorSchedule> schedules = repo.findByDoctorId(doctorId);

        return schedules.stream()
                .filter(schedule -> schedule.getAvailableTimeSlots().stream().anyMatch(slot -> !slot.isBlocked()))
                .map(DoctorSchedule::getDate)
                .collect(Collectors.toList());
    }

    public List<TimeSlot> getAvailableTimeSlotsForDoctorAndDate(Long doctorId, LocalDate date) {
        logger.info("Fetching available time slots for Doctor ID: {} on Date: {}", doctorId, date);
        DoctorSchedule doctorSchedule = repo.findByDoctorIdAndDate(doctorId, date)
                .orElseThrow(() -> new ResourceNotFoundException("No schedule found for the doctor on the given date"));

        return doctorSchedule.getAvailableTimeSlots().stream()
                .filter(timeSlot -> !timeSlot.isBlocked())
                .toList();
    }

    public List<DoctorSchedule> getAllDoctors() {
        logger.info("Fetching all doctors' schedules.");
        return repo.findAll();
    }

    public Optional<DoctorSchedule> getDoctorSchedule(Long doctorId, LocalDate date) {
        logger.info("Fetching schedule for Doctor ID: {} on Date: {}", doctorId, date);
        return repo.findById(new ScheduledId(doctorId, date));
    }

    public String createAvailability(Long doctorId) {
        logger.info("Creating availability for Doctor ID: {}", doctorId);
        String doctorName = repo.findNameByDoctorId(doctorId);
        if (doctorName == null || doctorName.isEmpty()) {
            logger.error("Doctor not found with ID: {}", doctorId);
            throw new IllegalArgumentException("Doctor not found with ID: " + doctorId);
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(6);

        List<TimeSlot> timeSlots = List.of(
                new TimeSlot(LocalTime.of(9, 0), false),
                new TimeSlot(LocalTime.of(10, 0), false),
                new TimeSlot(LocalTime.of(11, 0), false),
                new TimeSlot(LocalTime.of(13, 0), false),
                new TimeSlot(LocalTime.of(14, 0), false),
                new TimeSlot(LocalTime.of(15, 0), false)
        );

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DoctorSchedule sc = new DoctorSchedule(doctorId, doctorName, date, new ArrayList<>(timeSlots), false);
            repo.save(sc);
        }

        logger.info("Availability created successfully for Doctor ID: {}", doctorId);
        return "Availability Created Successfully";
    }

    public String blockAvailability(Long doctorID, LocalDate date) {
        logger.info("Blocking availability for Doctor ID: {} on Date: {}", doctorID, date);
        if (!userRepository.existsById(doctorID)) {
            logger.warn("Doctor ID not found: {}", doctorID);
            throw new ResourceNotFoundException("Doctor ID not found");
        }

        DoctorSchedule availability = repo.findById(new ScheduledId(doctorID, date)).orElse(null);
        if (availability != null) {
            if (availability.isIsblocked()) {
                return date + " is already blocked";
            }
            availability.setIsblocked(true);
            availability.getAvailableTimeSlots().forEach(slot -> slot.setBlocked(true));
            repo.save(availability);
            logger.info("Date {} blocked successfully for Doctor ID: {}", date, doctorID);
            return date + " is blocked successfully";
        }
        logger.warn("Availability does not exist for Date: {}", date);
        return "Availability does not exist";
    }

    public String unblockAvailability(Long doctorID, LocalDate date) {
        logger.info("Unblocking availability for Doctor ID: {} on Date: {}", doctorID, date);
        if (!userRepository.existsById(doctorID)) {
            logger.warn("Doctor ID does not exist: {}", doctorID);
            throw new ResourceNotFoundException("Doctor ID does not exist");
        }

        DoctorSchedule availability = repo.findById(new ScheduledId(doctorID, date)).orElse(null);
        if (availability != null) {
            if (!availability.isIsblocked()) {
                return date + " is already unblocked";
            }
            availability.setIsblocked(false);
            availability.getAvailableTimeSlots().forEach(slot -> slot.setBlocked(false));
            repo.save(availability);
            logger.info("Date {} unblocked successfully for Doctor ID: {}", date, doctorID);
            return date + " is unblocked successfully";
        }
        logger.warn("Availability does not exist for Date: {}", date);
        return "Availability does not exist";
    }
}
