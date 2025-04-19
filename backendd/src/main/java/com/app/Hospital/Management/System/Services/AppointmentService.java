package com.app.Hospital.Management.System.Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Hospital.Management.System.entities.Appointment;
import com.app.Hospital.Management.System.entities.AppointmentStatus;
import com.app.Hospital.Management.System.entities.DoctorSchedule;
import com.app.Hospital.Management.System.entities.PatientProfile;
import com.app.Hospital.Management.System.entities.ScheduledId;
import com.app.Hospital.Management.System.entities.TimeSlot;
import com.app.Hospital.Management.System.repositories.AppointmentRepository;
import com.app.Hospital.Management.System.repositories.DoctorScheduleRepository;
import com.app.Hospital.Management.System.repositories.PatientProfileRepository;

@Service
public class AppointmentService {
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private DoctorScheduleRepository doctorScheduleRepository;
	
	@Autowired
	private NotificationService notificationService;
	
//	public Appointment saveAppointment(Appointment appointment) {
//		System.out.println(appointment);
//        return appointmentRepository.save(appointment);
//    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
    
    public Appointment updateAppointmentStatus(Long id, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(newStatus);

        return appointmentRepository.save(appointment);
    }
    
    
    public Appointment updateAppointment(Long id, Appointment appointmentDetails) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        // Update patient association
        if (appointmentDetails.getPatient() != null) {
            appointment.setPatient(appointmentDetails.getPatient());
        }

        // Update doctor association
        if (appointmentDetails.getDoctor() != null) {
            appointment.setDoctor(appointmentDetails.getDoctor());
        }

        // Update other appointment details
        //appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
        appointment.setStatus(appointmentDetails.getStatus());
        
        return appointmentRepository.save(appointment);
    }
    
//    public Appointment updateAppointment(Long id, Appointment appointmentDetails) {
//        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment not found"));
//        appointment.setPatientID(appointmentDetails.getPatientID());
//        appointment.setDoctorID(appointmentDetails.getDoctorID());
//        appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
//        appointment.setStatus(appointmentDetails.getStatus());
//        return appointmentRepository.save(appointment);
//    }
    
    public List<Appointment> getAppointmentsByStatus(String status) {
    	 AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
    	    return appointmentRepository.findByStatus(appointmentStatus);
       // return appointmentRepository.findByStatus(status);
    }
    
    
    public String bookAppointment(Appointment appointment) {
    	System.out.println(appointment.toString());
        Long doctorId = appointment.getDoctor().getDoctorId();
        LocalDate date = appointment.getDoctor().getDate();
        LocalTime timeSlot = appointment.getAppointmentTime();

        Optional<DoctorSchedule> optionalSchedule = doctorScheduleRepository.findById(new ScheduledId(doctorId, date));
        if (optionalSchedule.isPresent()) {
            DoctorSchedule schedule = optionalSchedule.get();
            for (TimeSlot slot : schedule.getAvailableTimeSlots()) {
                if (slot.getTimeSlot().equals(timeSlot) && !slot.isBlocked()) {
                    slot.setBlocked(true);
                    doctorScheduleRepository.save(schedule);

                    // Save the appointment
                    appointmentRepository.save(appointment);

                    // Check if all slots are booked
                    boolean allSlotsBooked = schedule.getAvailableTimeSlots().stream().allMatch(TimeSlot::isBlocked);
                    if (allSlotsBooked) {
                        doctorScheduleRepository.delete(schedule);
                    }
                    
                    notificationService.createNotificationsForAppointment(appointment.getAppointmentId());

                    return "Appointment booked successfully.";
                }
            }
            return "Time slot not available.";
        }
        return "Doctor schedule not found.";
    }
    
    public String cancelAppointment(Long appointmentId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);
            
            DoctorSchedule sc=appointment.getDoctor();
            LocalTime aptime=appointment.getAppointmentTime();
            updateTimeSlotAvailability(sc,aptime,false);
            
            return "Appointment cancelled successfully";
        } else {
            return "Appointment not found";
        }
    }
    
    public String rescheduleAppointment(Long appointmentId, LocalDate newDate, LocalTime newTime) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            DoctorSchedule currentSchedule = appointment.getDoctor();
            LocalDate currentDate = currentSchedule.getDate();
            LocalTime currentTime = appointment.getAppointmentTime();

            // Set the current time slot to available
            updateTimeSlotAvailability(currentSchedule, currentTime, false);

            // Update the appointment with the new date and time
            DoctorSchedule newSchedule = doctorScheduleRepository.findByDoctorIdAndDate(appointment.getDoctor().getDoctorId(), newDate)
            		.orElseGet(() -> new DoctorSchedule(appointment.getDoctor().getDoctorId(),appointment.getDoctor().getName(), newDate, new ArrayList<>()));
            appointment.setDoctor(newSchedule);
            appointment.setAppointmentTime(newTime);
            appointment.setStatus(AppointmentStatus.SCHEDULED);

            // Set the new time slot to blocked
            updateTimeSlotAvailability(newSchedule, newTime, true);
            
            appointmentRepository.save(appointment);
            
            return "Appointment rescheduled successfully.";
            
        } else {
            return "Appointment not found";
        }
    }
    
    
    private void updateTimeSlotAvailability(DoctorSchedule schedule, LocalTime time, boolean isBlocked) {
        List<TimeSlot> timeSlots = schedule.getAvailableTimeSlots();
        for (TimeSlot slot : timeSlots) {
            if (slot.getTimeSlot().equals(time)) {
                slot.setBlocked(isBlocked);
                doctorScheduleRepository.save(schedule);
                return;
            }
        }
        // If the time slot does not exist, create a new one
//        TimeSlot newSlot = new TimeSlot(time, isBlocked);
//        timeSlots.add(newSlot);
//        doctorScheduleRepository.save(schedule);
    }
    
}
    
    
//    public String bookAppointment(Long patientId,Long doctorId, LocalDate date, LocalTime timeSlot) {
//        Optional<DoctorSchedule> optionalSchedule =repo.findById(new ScheduledId(doctorId, date));
//        Optional<PatientProfile> optionalpat= pat.findById(patientId);
//        if (optionalSchedule.isPresent()) {
//            DoctorSchedule schedule = optionalSchedule.get();
//            for (TimeSlot slot : schedule.getAvailableTimeSlots()) {
//                if (slot.getTimeSlot().equals(timeSlot) && !slot.isBlocked()) {
//                    slot.setBlocked(true);
//                    repo.save(schedule);
//                    
//                    // Check if all slots are booked
//                    boolean allSlotsBooked = schedule.getAvailableTimeSlots().stream().allMatch(TimeSlot::isBlocked);
//                    if (allSlotsBooked) {
//                    	repo.delete(schedule);
//                    }
//                    
//                    return "Appointment booked successfully.";
//                }
//            }
//            return "Time slot not available.";
//        }
//        return "Doctor schedule not found.";
//    }

