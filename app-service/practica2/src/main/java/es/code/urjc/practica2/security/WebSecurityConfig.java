package es.code.urjc.practica2.security;

import org.springframework.security.config.Customizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Autowired RepositoryUserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
    @Order(1) 
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**") 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/principal").permitAll() 
                .anyRequest().authenticated() 
            )
            .httpBasic(Customizer.withDefaults()) 
            .csrf(csrf -> csrf.disable()) 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				
				.requiresChannel(channel -> channel.anyRequest().requiresSecure())
				.authorizeHttpRequests(auth -> auth
						// Public pages
						.requestMatchers("/", "/css/**", "/js/**", "/images/**", "/posters/**", "/login", "/signUp",
								"/logout", "/principal", "/lists", "/series", "/films/recent",
								"/films/genre/**", "/series/genre/**", "/searchBar", "/filmographies/{id}",
								"/filmographies/{id}/reviews", "/lists/{id}", "/img/**", "/aboutUs", "/cookies",
								"/frequentlyAskedQuestions", "/legalAdvise", "/error", "/searchBar","/sendRecoveryEmail", "/restartPassword")
						.permitAll()

						// Specific roles
						.requestMatchers("/administrator/**", "/movies/**", "/series/**").hasRole("ADMIN")

						.requestMatchers("/profile/**", "/myReviews", "/reviews/**", "/myLists/**", "/lists/**",
								"/filmographies/*/reviews/new",
								"/filmographies/*/lists/update", "/filmographies/*/lists/new").hasAnyRole("USER", "ADMIN")

						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.defaultSuccessUrl("/principal", true)
						.failureUrl("/login?error")
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout") 
						.logoutSuccessUrl("/login") 
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.permitAll());

		http.userDetailsService(userDetailsService);

		return http.build();
	}
}
