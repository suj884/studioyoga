package studioyoga.project.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studioyoga.project.dto.ClassesDTO;
import studioyoga.project.model.AllowedClass;
import studioyoga.project.model.Classes;
import studioyoga.project.repository.ClassRepository;
import studioyoga.project.repository.ReservationRepository;

@Service
public class ClassesService {

	@Autowired
	private ClassRepository classRepository;

	@Autowired
	private ReservationService reservationService;
	@Autowired
	private ReservationRepository reservationRepository;

	public List<Classes> findAll() {
		return classRepository.findAll();
	}

	public Optional<Classes> findById(Integer id) {
		return classRepository.findById(id);
	}

	public Classes save(Classes classes) {
		return classRepository.save(classes);
	}

	public void deleteById(Integer id) {
		classRepository.deleteById(id);
	}

	public void deleteClassAndReservations(Integer classId) {
		reservationRepository.deleteByClassesId(classId); // Borra reservas asociadas
		classRepository.deleteById(classId); // Borra la clase
	}

	// Find upcoming classes (future events)
	public List<Classes> findUpcomingClasses() {
		return classRepository.findByEventDateAfterOrderByEventDateAsc(java.time.LocalDate.now());
	}

	// Find upcoming classes with available spots
	public List<ClassesDTO> findUpcomingClassesWithSpots() {
		List<Classes> classesList = classRepository.findByActiveTrueOrderByEventDateAsc();
		List<ClassesDTO> result = new ArrayList<>();
		for (Classes c : classesList) {
			int reserved = reservationService.countByClassId(c.getId());
			int spotsLeft = c.getCapacity() - reserved;
			result.add(new ClassesDTO(c, spotsLeft));
		}
		return result;
	}

   // Lista de combinaciones válidas de día, hora y nombre de clase
    private static final List<AllowedClass> scheduleValid = List.of(
		new AllowedClass("LUNES", "10:00", "Yoga Suave"),
        new AllowedClass("LUNES", "18:00", "Yoga Terapéutico"),
        new AllowedClass("LUNES", "20:00", "Hatha Yoga"),
        new AllowedClass("MARTES", "09:00", "Hatha Yoga"),
        new AllowedClass("MARTES", "17:00", "Yoga Kundalini"),
        new AllowedClass("MARTES", "18:00", "Hatha Yoga"),
        new AllowedClass("MARTES", "20:00", "Vinyasa Yoga"),
		new AllowedClass("MIERCOLES", "10:00", "Yoga Suave"),
        new AllowedClass("MIERCOLES", "18:00", "Yoga Terapéutico"),
        new AllowedClass("MIERCOLES", "20:00", "Hatha Yoga"),
        new AllowedClass("JUEVES", "09:00", "Hatha Yoga"),
        new AllowedClass("JUEVES", "17:00", "Yoga Kundalini"),
        new AllowedClass("JUEVES", "18:00", "Hatha Yoga"),
        new AllowedClass("JUEVES", "20:00", "Vinyasa Yoga"),
		new AllowedClass("VIERNES", "10:00", "Hatha y Meditacion")
     
    );
	    public boolean isAllowedSchedule(String day, String time, String className) {
        return scheduleValid.stream()
            .anyMatch(ac -> ac.day().equals(day) && ac.time().equals(time) && ac.className().equals(className));
    }

	public boolean existsByDateTime(LocalDate eventDate, LocalTime timeInit) {
    return classRepository.countByEventDateAndTimeInit(eventDate, timeInit) > 0;
}
public boolean existsByDateTimeExcludingId(LocalDate eventDate, LocalTime timeInit, Integer id) {
    return classRepository.countByEventDateAndTimeInitAndIdNot(eventDate, timeInit, id) > 0;
}



}
