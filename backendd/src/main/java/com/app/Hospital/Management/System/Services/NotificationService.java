package com.app.Hospital.Management.System.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Hospital.Management.System.entities.Appointment;
import com.app.Hospital.Management.System.entities.DoctorSchedule;
import com.app.Hospital.Management.System.entities.Notification;
import com.app.Hospital.Management.System.entities.PatientProfile;
import com.app.Hospital.Management.System.repositories.AppointmentRepository;
import com.app.Hospital.Management.System.repositories.NotificationRepository;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    public Notification saveNotification(Notification notification) {
        logger.info("Saving new notification: {}", notification.getMessage());
        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        logger.info("Fetching all notifications.");
        return notificationRepository.findAll();
    }

    public Optional<Notification> getNotificationById(Long id) {
        logger.info("Fetching notification with ID: {}", id);
        return notificationRepository.findById(id);
    }

    public void deleteNotificationById(Long id) {
        logger.warn("Deleting notification with ID: {}", id);
        notificationRepository.deleteById(id);
    }

    public void createPatientNotification(Long appointmentID) {
        logger.info("Creating patient notification for Appointment ID: {}", appointmentID);
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentID)
                .orElseThrow(() -> {
                    logger.error("Appointment not found for ID: {}", appointmentID);
                    return new RuntimeException("Appointment not found with ID: " + appointmentID);
                });

        PatientProfile patient = appointment.getPatient();
        DoctorSchedule doctor = appointment.getDoctor();
        
        if (patient == null || doctor == null) {
            logger.error("Patient or Doctor details missing for Appointment ID: {}", appointmentID);
            throw new RuntimeException("Missing details for Appointment ID: " + appointmentID);
        }

        Notification patientNotification = new Notification();
        patientNotification.setAppointment(appointment);
        patientNotification.setMessage("Dear " + patient.getName() + ", your appointment with Dr. " + doctor.getName()
                + " is " + appointment.getStatus() + " on " + doctor.getDate()
                + " at " + appointment.getAppointmentTime() + ". Appointment ID: " + appointmentID + ".");
        patientNotification.setTimeStamp(LocalDateTime.now());
        notificationRepository.save(patientNotification);
    }

    public void createDoctorNotification(Long appointmentID) {
        logger.info("Creating doctor notification for Appointment ID: {}", appointmentID);
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentID)
                .orElseThrow(() -> {
                    logger.error("Appointment not found for ID: {}", appointmentID);
                    return new RuntimeException("Appointment not found with ID: " + appointmentID);
                });

        PatientProfile patient = appointment.getPatient();
        DoctorSchedule doctor = appointment.getDoctor();
        
        if (patient == null || doctor == null) {
            logger.error("Patient or Doctor details missing for Appointment ID: {}", appointmentID);
            throw new RuntimeException("Missing details for Appointment ID: " + appointmentID);
        }

        Notification doctorNotification = new Notification();
        doctorNotification.setAppointment(appointment);
        doctorNotification.setMessage("Dr. " + doctor.getName() + ", you have an appointment with " + patient.getName()
                + " on " + doctor.getDate() + " at " + appointment.getAppointmentTime()
                + ". Appointment ID: " + appointmentID + ".");
        doctorNotification.setTimeStamp(LocalDateTime.now());
        notificationRepository.save(doctorNotification);
    }
    
    public List<Notification> getNotificationsByPatientId(Long patientId) {
    	return notificationRepository.findNotificationsByPatientId(patientId); 	
    }
    
    public List<Notification> getNotificationsByDoctorId(Long doctorId) {
    	return notificationRepository.findNotificationsByDoctorId(doctorId); 
    }

    public void generateRemindersForAppointments() {
        logger.info("Generating appointment reminders.");
        List<Appointment> appointments = appointmentRepository.findAll();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Appointment> tomorrowAppointments = appointments.stream()
                .filter(appointment -> appointment.getDoctor().getDate().equals(tomorrow))
                .collect(Collectors.toList());

        for (Appointment appointment : tomorrowAppointments) {
            Notification patientReminder = new Notification();
            patientReminder.setAppointment(appointment);
            patientReminder.setMessage("Dear Patient, this is a reminder for your appointment with Dr. "
                    + appointment.getDoctor().getName() + " on " + tomorrow
                    + ". Your appointment ID is: " + appointment.getAppointmentId() + ".");
            patientReminder.setTimeStamp(LocalDateTime.now());
            notificationRepository.save(patientReminder);

            Notification doctorReminder = new Notification();
            doctorReminder.setAppointment(appointment);
            doctorReminder.setMessage("Dear Doctor, this is a reminder for your appointment with " + appointment.getPatient().getName()
                    + " on " + tomorrow + ". Appointment ID: " + appointment.getAppointmentId() + ".");
            doctorReminder.setTimeStamp(LocalDateTime.now());
            notificationRepository.save(doctorReminder);

            logger.info("Reminder created for Appointment ID: {}", appointment.getAppointmentId());
        }
    }
}
