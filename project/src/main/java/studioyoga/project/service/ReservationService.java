package studioyoga.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import studioyoga.project.exception.AlreadyReservedException;
import studioyoga.project.exception.NoSpotsAvailableException;
import studioyoga.project.model.Classes;
import studioyoga.project.model.Reservation;
import studioyoga.project.model.User;
import studioyoga.project.repository.ClassRepository;
import studioyoga.project.repository.ReservationRepository;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private ClassRepository classRepository;

	// List all reservations (admin)
	
	public List<Reservation> findByUserOrClass(String usuario, String clase) {
		if (usuario != null && !usuario.isEmpty() && clase != null && !clase.isEmpty()) {
			return reservationRepository.findByUserNameContainingIgnoreCaseAndClassesTitleContainingIgnoreCase(usuario,
					clase);
		} else if (usuario != null && !usuario.isEmpty()) {
			return reservationRepository.findByUserNameContainingIgnoreCase(usuario);
		} else if (clase != null && !clase.isEmpty()) {
			return reservationRepository.findByClassesTitleContainingIgnoreCase(clase);
		} else {
			return reservationRepository.findAll();
		}
	}

	// Find reservation by ID
	public Optional<Reservation> findById(Integer id) {
		return reservationRepository.findById(id);
	}

	// Save or update reservation (admin)
	public Reservation save(Reservation reservation) {
		return reservationRepository.save(reservation);
	}

	// Delete reservation by ID (admin)
	public void deleteById(Integer id) {
		reservationRepository.deleteById(id);
	}

	// Find reservations by class
	public List<Reservation> findByClassId(Integer classId) {
		return reservationRepository.findByClassesId(classId);
	}

	// Find reservations by user
	public List<Reservation> findByUser(User user) {
		return reservationRepository.findByUser(user);
	}

	// Count reservations for a class
	public int countByClassId(Integer classId) {
		return reservationRepository.findByClassesId(classId).size();
	}

	// Reserve a class for a user
	public void reserveClass(User user, Integer classId) throws NoSpotsAvailableException, AlreadyReservedException {
		Classes yogaClass = classRepository.findById(classId)
				.orElseThrow(() -> new RuntimeException("Class not found"));

		// Check available spots
		int reservedCount = reservationRepository.findByClassesId(yogaClass.getId()).size();
		if (reservedCount >= yogaClass.getCapacity()) {
			throw new NoSpotsAvailableException();
		}

		// Check if already reserved
		boolean alreadyReserved = reservationRepository.findByUser(user).stream()
				.anyMatch(r -> r.getClasses().getId().equals(yogaClass.getId()));
		if (alreadyReserved) {
			throw new AlreadyReservedException();
		}

		// Create and save reservation
		Reservation reservation = new Reservation();
		reservation.setUser(user);
		reservation.setClasses(yogaClass);
		reservation.setDateReservation(LocalDateTime.now());
		reservationRepository.save(reservation);
	}

	// Cancel reservation
	public void cancelReservation(User user, Integer reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new RuntimeException("Reservation not found"));
		if (!reservation.getUser().getId().equals(user.getId())) {
			throw new RuntimeException("You can't cancel another user's reservation");
		}
		reservationRepository.delete(reservation);
	}

	// Toggle reservation active status (if needed)
	public void toggleActive(Integer id) {
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Reservation not found"));
		reservation.setActive(!reservation.isActive());
		reservationRepository.save(reservation);
	}

	// Update reservation (change class)
	public void updateReservation(User user, Integer reservationId, Integer newClassId) {
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new RuntimeException("Reservation not found"));
		if (!reservation.getUser().getId().equals(user.getId())) {
			throw new RuntimeException("You can't modify another user's reservation");
		}
		Classes newClass = classRepository.findById(newClassId)
				.orElseThrow(() -> new RuntimeException("Class not found"));
		reservation.setClasses(newClass);
		reservationRepository.save(reservation);
	}

	// Find reservation by ID and user
	public Reservation findByIdAndUser(Integer reservationId, User user) {
		return reservationRepository.findByIdAndUser(reservationId, user)
				.orElseThrow(() -> new RuntimeException("Reservation not found for this user"));
	}

	public List<Reservation> findAll() {
		return reservationRepository.findAll();
	}

}
