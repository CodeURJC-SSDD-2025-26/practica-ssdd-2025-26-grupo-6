package es.code.urjc.practica2.security;

import es.code.urjc.practica2.security.jwt.JwtRequestFilter;
import es.code.urjc.practica2.security.jwt.JwtTokenProvider;
import es.code.urjc.practica2.security.jwt.UnauthorizedHandlerJwt;
import org.springframework.security.config.Customizer;

import javax.management.relation.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.code.urjc.practica2.model.Account;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Autowired RepositoryUserDetailsService userDetailsService;
	@Autowired JwtTokenProvider jwtTokenProvider;
	@Autowired private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
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
		http.authenticationProvider(authenticationProvider());
		http
			.securityMatcher("/api/**", "/v3/api-docs/**", "/v3/api-docs")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));
        
		http
            .authorizeHttpRequests(auth -> auth
				.requestMatchers("/v3/api-docs*/**", "/v3/api-docs").permitAll()
            	.requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/auth/login").permitAll()
                .requestMatchers("/api/v1/auth/refresh").permitAll()
                .requestMatchers("/api/v1/auth/logout").permitAll()
				.requestMatchers("/api/v1/auth/signup").permitAll()
                .requestMatchers("/api/v1/principal", "/api/v1/series", "/api/v1/lists").permitAll()
				.requestMatchers("/api/v1/administrator/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            );

        http.formLogin(form -> form.disable());
        http.csrf(csrf -> csrf.disable());
        http.httpBasic(basic -> basic.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(
            new JwtRequestFilter(userDetailsService, jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.requiresChannel(channel -> channel.anyRequest().requiresSecure())
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/", "/css/**", "/js/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs*/**").permitAll()
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
