package com.app.Hospital.Management.System.Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.Hospital.Management.System.entities.*;
import com.app.Hospital.Management.System.exceptions.ResourceNotFoundException;
import com.app.Hospital.Management.System.repositories.*;

@Service
public class AppointmentService {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientProfileRepository patientProfileRepository;
    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;
    @Autowired
    private NotificationService notificationService;

    public List<Appointment> getAllAppointments() {
        logger.info("Fetching all appointments.");
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        logger.info("Fetching appointment with ID: {}", id);
        return appointmentRepository.findById(id);
    }

    public void deleteAppointment(Long id) {
        logger.warn("Deleting appointment with ID: {}", id);
        appointmentRepository.deleteById(id);
    }
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
    	return appointmentRepository.findByPatientId(patientId);
    }
    
    public Appointment updateAppointmentStatus(Long id, AppointmentStatus newStatus) {
        logger.info("Updating appointment status for ID: {} to {}", id, newStatus);
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
        appointment.setStatus(newStatus);
        return appointmentRepository.save(appointment);
    }

    public String bookAppointment(Appointment appointment) {
        logger.info("Booking appointment for patient: {} at {}", appointment.getPatient().getPatientId(), appointment.getAppointmentTime());
        Long doctorId = appointment.getDoctor().getDoctorId();
        LocalDate date = appointment.getDoctor().getDate();
        LocalTime timeSlot = appointment.getAppointmentTime();

        PatientProfile patient = patientProfileRepository.findById(appointment.getPatient().getPatientId())
            .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + appointment.getPatient().getPatientId()));

        DoctorSchedule doctorSchedule = doctorScheduleRepository.findById(new ScheduledId(doctorId, date))
            .orElseThrow(() -> new RuntimeException("Doctor schedule not found for ID: " + doctorId + " and date: " + date));

        appointment.setPatient(patient);
        appointment.setDoctor(doctorSchedule);

        for (TimeSlot slot : doctorSchedule.getAvailableTimeSlots()) {
            if (slot.getTimeSlot().equals(timeSlot) && !slot.isBlocked()) {
                slot.setBlocked(true);
                doctorScheduleRepository.save(doctorSchedule);
                appointmentRepository.save(appointment);

                notificationService.createPatientNotification(appointment.getAppointmentId());
                notificationService.createDoctorNotification(appointment.getAppointmentId());

                logger.info("Appointment booked successfully for patient ID: {}", appointment.getPatient().getPatientId());
                return "Appointment booked successfully.";
            }
        }

        logger.warn("Time slot {} is not available for Doctor ID: {}", timeSlot, doctorId);
        return "Time slot not available.";
    }

    public String cancelAppointment(Long appointmentId) {
        logger.info("Cancelling appointment with ID: {}", appointmentId);
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        updateTimeSlotAvailability(appointment.getDoctor(), appointment.getAppointmentTime(), false);
        logger.info("Appointment cancelled successfully.");
        return "Appointment cancelled successfully.";
    }

    private void updateTimeSlotAvailability(DoctorSchedule schedule, LocalTime time, boolean isBlocked) {
        logger.info("Updating time slot availability for doctor schedule ID: {}", schedule.getDoctorId());
        for (TimeSlot slot : schedule.getAvailableTimeSlots()) {
            if (slot.getTimeSlot().equals(time)) {
                slot.setBlocked(isBlocked);
                doctorScheduleRepository.save(schedule);
                return;
            }
        }
        logger.warn("Time slot {} not found in schedule.", time);
    }
}
