// package com.app.Hospital.Management.System.SecurityConfig;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.factory.PasswordEncoderFactories;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import com.app.Hospital.Management.System.utils.JwtRequestFilter;
// import static org.springframework.security.config.Customizer.withDefaults;

// import java.util.Arrays;
// @Configuration
// public class SecurityConfig {
	
// 	private final SecurityConfigPatient patientDetails;
// 	private JwtRequestFilter jwtRequestFilter;
// 	public SecurityConfig(SecurityConfigPatient patientDetails) {
// 		this.patientDetails=patientDetails;
// 		this.jwtRequestFilter=jwtRequestFilter;
// 	}
// 	@Bean
// 	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
// 		http
// 		.csrf(cust->cust.disable())
// 		.cors(withDefaults())
// 		.authorizeHttpRequests(auth->auth
// //				.requestMatchers("/api/hospital/patients/put/**","/api/hospital/patients/get/**","/api/hospital/patients/del/**","/api/hospital/appointments/**","/api/hospital/appointments/**").hasRole("PATIENT")
// //				.requestMatchers("/api/hospital/doctors/**","/api/hospital/doctors/create/**","/api/hospital/patients/findall","/api/hospital/history/save").hasRole("DOCTOR")
// //				.requestMatchers("/api/register/**").permitAll()
// //				.anyRequest().authenticated()
// 				//.requestMatchers("/api/hospital/patients/get/**").authenticated()
// 				.anyRequest().permitAll()
// 				).formLogin(withDefaults())
// 				.httpBasic(withDefaults())
// 				.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
// 			.formLogin(form -> form
//                 .loginPage("/login")
//                 .defaultSuccessUrl("/", true)
//                 .permitAll()
//             )
//             .logout(logout -> logout
//                 .logoutUrl("/logout")
//                 .logoutSuccessUrl("/login?logout")
//                 .permitAll()
//             )
//             .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
// 		return http.build();	
// 	}

// 	// @Bean
//     // public CorsFilter corsFilter() {
//     //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//     //     CorsConfiguration config = new CorsConfiguration();
//     //     config.setAllowCredentials(true);
//     //     config.addAllowedOriginPattern("*"); // Allow all origins
//     //     config.addAllowedHeader("*"); // Allow all headers
//     //     config.addAllowedMethod("*"); // Allow all methods
//     //     source.registerCorsConfiguration("/**", config);
//     //     return new CorsFilter(source);
//     // }

// 	// private UrlBasedCorsConfigurationSource corsConfigurationSource() {
//     //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//     //     CorsConfiguration config = new CorsConfiguration();
//     //     config.setAllowCredentials(true);
//     //     config.addAllowedOriginPattern("*"); // Allow all origins
//     //     config.addAllowedHeader("*"); // Allow all headers
//     //     config.addAllowedMethod("*"); // Allow all methods
//     //     source.registerCorsConfiguration("/**", config);
//     //     return source;
//     // }
	
// 	 @Bean
// 	    public CorsConfigurationSource corsConfigurationSource() {
// 	        CorsConfiguration configuration = new CorsConfiguration();
// 	        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Allow Angular frontend
// 	        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS"));
// 	        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
// 	        configuration.setExposedHeaders(Arrays.asList("Authorization"));
// 	        configuration.setAllowCredentials(true);

// 	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
// 	        source.registerCorsConfiguration("/**", configuration);
// 	        return source;
// 	    }

// 		 @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//         return config.getAuthenticationManager();
//     }
	
// 	@Bean
// 	public PasswordEncoder passwordEncoder() {
// 		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
// 	}
// }

package com.app.Hospital.Management.System.SecurityConfig;

import com.app.Hospital.Management.System.utils.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    private final SecurityConfigPatient patientDetails;
    private final JwtRequestFilter jwtRequestFilter;

    
    public SecurityConfig(SecurityConfigPatient patientDetails, JwtRequestFilter jwtRequestFilter) {
        this.patientDetails = patientDetails;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(withDefaults())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .formLogin(withDefaults())
            .httpBasic(withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
