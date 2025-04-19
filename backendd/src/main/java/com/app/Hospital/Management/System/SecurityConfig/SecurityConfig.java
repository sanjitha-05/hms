package com.app.Hospital.Management.System.SecurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
@Configuration
public class SecurityConfig {
	
	private final SecurityConfigPatient patientDetails;
	public SecurityConfig(SecurityConfigPatient patientDetails) {
		this.patientDetails=patientDetails;
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		.csrf(cust->cust.disable())
		.cors(withDefaults())
		.authorizeHttpRequests(auth->auth
//				.requestMatchers("/api/hospital/patients/put/**","/api/hospital/patients/get/**","/api/hospital/patients/del/**","/api/hospital/appointments/**","/api/hospital/appointments/**").hasRole("PATIENT")
//				.requestMatchers("/api/hospital/doctors/**","/api/hospital/doctors/create/**","/api/hospital/patients/findall","/api/hospital/history/save").hasRole("DOCTOR")
//				.requestMatchers("/api/register/**").permitAll()
//				.anyRequest().authenticated()
				.requestMatchers("/api/hospital/patients/get/**").authenticated()
				.anyRequest().permitAll()
				).formLogin(withDefaults())
				.httpBasic(withDefaults())
				.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();	
	}
	
	 @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Allow Angular frontend
	        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
	        configuration.setExposedHeaders(Arrays.asList("Authorization"));
	        configuration.setAllowCredentials(true);

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
