package com.app.Hospital.Management.System.SecurityConfig;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.Hospital.Management.System.entities.DoctorSchedule;
import com.app.Hospital.Management.System.entities.PatientProfile;
import com.app.Hospital.Management.System.repositories.DoctorScheduleRepository;
import com.app.Hospital.Management.System.repositories.PatientProfileRepository;
import com.app.Hospital.Management.System.repositories.UserRepository;
import com.app.Hospital.Management.System.entities.*;
@Service
public class SecurityConfigPatient implements UserDetailsService {
	@Autowired
	UserRepository userRepo;
	@Autowired
	PatientProfileRepository patRepo;

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User u=null;
		String role="";
		List<GrantedAuthority> li;
		if(username.contains("@doctor.com"))
		{	
			u=userRepo.findByEmail(username).get();
			role="ROLE_DOCTOR"+"";
			li=List.of(new SimpleGrantedAuthority(role));
			//System.out.println(u.getPassword());
			return new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPassword(),li);
		}
		else {
			u=userRepo.findByEmail(username).get();
			role="ROLE_PATIENT"+"";
			li=List.of(new SimpleGrantedAuthority(role));
			return new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPassword(),li);
		}
	}


}
