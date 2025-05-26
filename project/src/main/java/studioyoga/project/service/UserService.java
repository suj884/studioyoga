package studioyoga.project.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import studioyoga.project.model.User;
import studioyoga.project.repository.UserRepository;

@Service
public class UserService {

	@Autowired
private ReservationService reservationService;

@Autowired
private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findById(Integer id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
	}

	public void deleteById(Integer id) {
		userRepository.deleteById(id);
	}

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public void save(User user) {
		if (user.getId() == null || (user.getPassword() != null && !user.getPassword().isEmpty())) {
			// Solo encripta si la contraseña NO está ya encriptada (BCrypt empieza por $2a$
			// o $2b$)
			String password = user.getPassword();
			if (password != null && !password.startsWith("$2a$") && !password.startsWith("$2b$")) {
				user.setPassword(passwordEncoder.encode(password));
			}
			// Si ya está encriptada, la dejamos tal cual
		} else {
			// Edición y no se cambió la contraseña
			User existingUser = userRepository.findById(user.getId()).orElseThrow();
			user.setPassword(existingUser.getPassword());
		}
		userRepository.save(user);
	}

	public void updatePassword(String email, String newPassword) {
		User user = userRepository.findByEmail(email).orElseThrow();
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}
	public List<User> findUsersBySurnameAndNameAndRole(String name, String role) {
	    List<User> users;

	    boolean hasName = (name != null && !name.trim().isEmpty());
	    boolean hasRole = (role != null && !role.trim().isEmpty());

	    if (hasName && hasRole) {
	        // Buscar por nombre/apellidos y luego filtrar por rol
	        users = userRepository
	            .findByFirstLastNameContainingIgnoreCaseOrSecondLastNameContainingIgnoreCaseOrNameContainingIgnoreCase(
	                name, name, name)
	            .stream()
	            .filter(u -> u.getRol() != null && role.equalsIgnoreCase(u.getRol().getName()))
	            .collect(Collectors.toList());
	    } else if (hasName) {
	        users = userRepository
	            .findByFirstLastNameContainingIgnoreCaseOrSecondLastNameContainingIgnoreCaseOrNameContainingIgnoreCase(
	                name, name, name);
	    } else if (hasRole) {
	        users = userRepository.findByRol_Name(role);
	    } else {
	        users = userRepository.findAll();
	    }

	    // Ordenar por apellidos y nombre
	    users.sort(Comparator
	        .comparing(User::getFirstLastName, String.CASE_INSENSITIVE_ORDER)
	        .thenComparing(User::getSecondLastName, String.CASE_INSENSITIVE_ORDER)
	        .thenComparing(User::getName, String.CASE_INSENSITIVE_ORDER)
	    );

	    return users;
	}
@Transactional
public void deleteUserAndReservations(Integer userId) {
    reservationService.deleteByUserId(userId); // Elimina todas las reservas del usuario
    userRepository.deleteById(userId);         // Ahora puedes borrar el usuario
}
}