package co.edu.unbosque.empareja2.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unbosque.empareja2.entity.Student;
import co.edu.unbosque.empareja2.repository.StudentRepository;

/**
 * Puente entre Spring Security y nuestra entidad Student. El "username" del
 * sistema es el email del estudiante.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final StudentRepository studentRepository;

	public CustomUserDetailsService(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Student student = studentRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("No existe un usuario con el correo: " + email));

		return User.builder().username(student.getEmail()).password(student.getPassword())
				.authorities("ROLE_" + student.getRole().name()).build();
	}
}
