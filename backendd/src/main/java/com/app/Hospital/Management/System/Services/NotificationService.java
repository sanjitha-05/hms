package com.app.Hospital.Management.System.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Hospital.Management.System.entities.Appointment;
import com.app.Hospital.Management.System.entities.Notification;
import com.app.Hospital.Management.System.repositories.AppointmentRepository;
import com.app.Hospital.Management.System.repositories.NotificationRepository;

@Service
public class NotificationService {
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	public Notification saveNotification(Notification n) {
		return notificationRepository.save(n);
	}
	
	public List<Notification> getAllNotifications(){
		return notificationRepository.findAll();
	}
	public Optional<Notification> getNotificationById(Long id){
		return notificationRepository.findById(id);
	}
	
	public void deleteNotificationById(Long id) {
		notificationRepository.deleteById(id);
    }
	
	 public void createNotificationsForAppointment(Long appointmentID) {
	        // Fetch the appointment using the appointmentID
	        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentID).get();
	               // .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentID));

	        // Create and save a notification for the patient
	        Notification patientNotification = new Notification();
	        patientNotification.setAppointment(appointment);
	        patientNotification.setMessage("Dear Patient, your appointment with Doctor ID " + appointment.getDoctor().getDoctorId() + " is "+appointment.getStatus()+" on the date " +appointment.getDoctor().getDate()+" and the time "+ appointment.getAppointmentTime() + " and your appointment ID: " + appointmentID + ".");
	        patientNotification.setTimeStamp(LocalDateTime.now());
	        notificationRepository.save(patientNotification);

	        // Create and save a notification for the doctor
	        Notification doctorNotification = new Notification();
	        doctorNotification.setAppointment(appointment);
	        doctorNotification.setMessage("Dear Doctor, you have an appointment with Patient " + appointment.getPatient().getName() + " on the date " +appointment.getDoctor().getDate()+" and the time " + appointment.getAppointmentTime() + " with Appointment ID: " + appointmentID + ".");
	        doctorNotification.setTimeStamp(LocalDateTime.now());
	        notificationRepository.save(doctorNotification);
	    }
	 
	 
	 public void generateRemindersForAppointments() {
		    
		    List<Appointment> appointments = appointmentRepository.findAll();

		    LocalDate tomorrow = LocalDate.now().plusDays(1);
		    List<Appointment> tomorrowAppointments = appointments.stream()
		            .filter(appointment -> appointment.getDoctor().getDate().equals(tomorrow))
		            .collect(Collectors.toList());

		    for (Appointment appointment : tomorrowAppointments) {
		        Notification patientReminder = new Notification();
		        patientReminder.setAppointment(appointment);
		        patientReminder.setMessage("Dear Patient, this is a reminder for your appointment with Doctor ID " + appointment.getDoctor() + " scheduled for " + appointment.getDoctor().getDate() 
		                                   	+ ". Your appointment ID is: " + appointment.getAppointmentId() + ".");
		        patientReminder.setTimeStamp(LocalDateTime.now());
		        System.out.println("Generated Message: " + patientReminder.getMessage());
		        notificationRepository.save(patientReminder);
		        
		        Notification doctorReminder = new Notification();
		        doctorReminder.setAppointment(appointment);
		        doctorReminder.setMessage("Dear Doctor, this is a reminder for your appointment with Patient ID " + appointment.getPatient() 
		                                  + " scheduled for " + appointment.getDoctor().getDate()
		                                  + ". Appointment ID: " + appointment.getAppointmentId() + ".");
		        doctorReminder.setTimeStamp(LocalDateTime.now());
		        notificationRepository.save(doctorReminder);
		    }
		}
}
