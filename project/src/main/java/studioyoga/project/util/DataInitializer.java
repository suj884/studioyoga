package studioyoga.project.util;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import studioyoga.project.model.Rol;
import studioyoga.project.model.User;
// import studioyoga.project.model.enums.RoleEnum;
import studioyoga.project.repository.RolRepository;
import studioyoga.project.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
	private final RolRepository rolRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public DataInitializer(RolRepository rolRepository, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		this.rolRepository = rolRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

@Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        Rol adminRol = rolRepository.findByName("ADMIN")
                .orElseGet(() -> rolRepository.save(new Rol(null, "ADMIN")));
        Rol userRol = rolRepository.findByName("USER")
                .orElseGet(() -> rolRepository.save(new Rol(null, "USER")));

        // Usuarios con los mismos datos y hashes que tu SQL
        createUserIfNotExists("admin@example.com", "Admin", "Principal", null, null, true, 
            "$2a$10$J64YiyHUxUN.j0yO5EwvSel.MSeTlcQUczxqEGi1bQzRF1Sn8A312", null, null, 
            LocalDate.of(2025, 5, 8), null, null, adminRol);

        createUserIfNotExists("usuario@example.com", "Usuario", "Normal", null, null, true, 
            "$2a$10$3gQofPB3SGOoZqh8RJXnlOEoAQE872ZVA1q1DWiqVbdyq911S8YlW", null, null, 
            LocalDate.of(2025, 5, 8), null, null, userRol);

        createUserIfNotExists("ana@example.com", "Ana", "López", "Pérez", "", true, 
            "$2a$10$Jj.mFKsO.TyoQ82RKVDbNONRdp2GSWi5Fc4IvZUIg3VoblW5R3ElW", "600123456", 
            "https://res.cloudinary.com/dpimnxbbk/image/upload/v1746912440/mujer2_gq3pw7.jpg", 
            LocalDate.of(2025, 5, 8), "Pérez", "", userRol);

        createUserIfNotExists("carlos@example.com", "Carlos", "García", null, "", true, 
            "$2a$10$2.hXmk85.C9EMjsK4DZAqeX7iqtm5vd0TkVRJn3W81ermZjQn8Yfa", "600654321", 
            "https://res.cloudinary.com/dpimnxbbk/image/upload/v1746912437/hombre4_nmnzgl.jpg", 
            LocalDate.of(2025, 5, 8), "", "", userRol);

        createUserIfNotExists("maria@example.com", "María", "Fernández", "Gómez", "", true, 
            "$2a$10$NPUfvsTXaqSSg/t0/ADeMuGPY04rxqRPVuIJwTAMcY6jlGIt4dbha", "600987654", 
            "https://res.cloudinary.com/dpimnxbbk/image/upload/v1746912441/mujer3_gl86fd.jpg", 
            LocalDate.of(2025, 5, 8), "Gómez", "", userRol);

        createUserIfNotExists("luis@example.com", "Luis", "Martínez", "Sánchez", "", true, 
            "$2a$10$7zoNWkR5K3YLWpoI5UIk6eX8bKooinLNH1hovTxhQPJI.R1vZ49N.", "600456789", 
            "https://res.cloudinary.com/dpimnxbbk/image/upload/v1746912436/hombre2_xawn7u.jpg", 
            LocalDate.of(2025, 5, 8), "Sánchez", "", userRol);

        createUserIfNotExists("elena@example.com", "Elena", "Ruiz", "Hernández", "", true, 
            "$2a$10$HaMSs9UGZo/pO7M1NjLmb.iogeeadbaoYJAYYjvM3Fzc6qjHu4mXi", "600321654", 
            "https://res.cloudinary.com/dpimnxbbk/image/upload/v1746912437/mujer1_sv6axr.jpg", 
            LocalDate.of(2025, 5, 8), "Hernández", "", userRol);

        createUserIfNotExists("javier@example.com", "Javier", "Pérez", "López", "", true, 
            "$2a$10$MPAFySsUWpMheDqPZHWL2uOZrewVNE/TvFh/hGBSKtlRP1jQ6rEYG", "600789123", 
            "https://res.cloudinary.com/dpimnxbbk/image/upload/v1746912437/hombre3_lwnanf.jpg", 
            LocalDate.of(2025, 5, 10), "López", "", userRol);

        createUserIfNotExists("antonio@example.com", "Antonio", "Garcia", "Garcia", "", true, 
            "$2a$10$dCTf84P6i5dtAy.5tiOgHecPVBYrX6eMVOlWjtIMH5XryQW0CRqsS", "666555444", 
            "https://res.cloudinary.com/dpimnxbbk/image/upload/v1746912436/hombre1_f01jj0.jpg", 
            LocalDate.of(2025, 5, 10), "Garcia", "", userRol);

        createUserIfNotExists("gema@example.com", "Gema", "Rodriguez", "Rodriguez", "", true, 
            "$2a$10$s5f4S.U9lmx6E68eI8n9AuImli05WwLg/55P6rPq74CQt0ToK4SKW", "666555333", 
            "https://res.cloudinary.com/dpimnxbbk/image/upload/v1746912442/mujer5_jazirk.jpg", 
            LocalDate.of(2025, 5, 11), "Rodriguez", "", userRol);
    }

    private void createUserIfNotExists(String email, String name, String firstLastName, String secondLastName, String notes, boolean enabled,
                                       String encodedPassword, String phoneNumber, String profilePicture, LocalDate registrationDate,
                                       String verificationCode, String extra, Rol rol) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setFirstLastName(firstLastName);
            user.setSecondLastName(secondLastName);
            user.setNotes(notes);
            user.setEnabled(enabled);
            user.setPassword(encodedPassword); // ¡No volver a encriptar!
            user.setPhoneNumber(phoneNumber);
            user.setProfilePicture(profilePicture);
            user.setRegistrationDate(registrationDate);
            user.setVerificationCode(verificationCode);
            user.setRol(rol);
            userRepository.save(user);
        }
    }
}
