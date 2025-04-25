package com.app.Hospital.Management.System.entities;
 
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
//import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
//import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
//import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
 
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="patients")
@ToString(exclude = "medicalHistory")
public class PatientProfile {
    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "patientid", updatable=false)
    private Long patientId;
    
    @NotBlank(message = "Name is mandatory")
    @Column(nullable=false)
    private String name;
    
    @NotNull(message = "Date of birth is mandatory")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<MedicalHistory> medicalHistory;
    
    @Pattern(regexp="^[6-9]\\d{9}$", message="Contact details must be exactly 10 digits")
    @NotBlank(message = "Contact details are mandatory")
    @Size(min = 10, max=10, message = "Contact details must be 10 characters long")
    @Column(nullable=false)
    private String contactDetails;
  
}