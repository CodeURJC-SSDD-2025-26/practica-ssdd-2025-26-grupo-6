package es.code.urjc.practica2.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.repository.AccountRepository;

@Service
public class RepositoryUserDetailsService implements UserDetailsService {
	@Autowired private AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Account account = accountRepository.findByAccountEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("ROLE_" + account.getAccountRole().name()));

		return new org.springframework.security.core.userdetails.User(account.getAccountEmail(), account.getAccountPassword(), roles);
	}
}
