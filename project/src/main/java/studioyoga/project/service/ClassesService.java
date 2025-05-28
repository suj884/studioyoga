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

/**
 * Servicio para la gestión de clases de yoga.
 * Proporciona métodos para listar, buscar, crear, editar, eliminar clases,
 * validar horarios permitidos y comprobar disponibilidad de plazas.
 */
@Service
public class ClassesService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Obtiene la lista de todas las clases registradas.
     *
     * @return Lista de clases.
     */
    public List<Classes> findAll() {
        return classRepository.findAll();
    }

    /**
     * Busca una clase por su ID.
     *
     * @param id ID de la clase.
     * @return Un {@link Optional} con la clase si existe, o vacío si no se encuentra.
     */
    public Optional<Classes> findById(Integer id) {
        return classRepository.findById(id);
    }

    /**
     * Guarda o actualiza una clase.
     *
     * @param classes Objeto Classes a guardar.
     * @return La clase guardada o actualizada.
     */
    public Classes save(Classes classes) {
        return classRepository.save(classes);
    }

    /**
     * Elimina una clase por su ID.
     *
     * @param id ID de la clase a eliminar.
     */
    public void deleteById(Integer id) {
        classRepository.deleteById(id);
    }

    /**
     * Elimina una clase y todas las reservas asociadas a ella.
     *
     * @param classId ID de la clase a eliminar.
     */
    public void deleteClassAndReservations(Integer classId) {
        reservationRepository.deleteByClassesId(classId); // Borra reservas asociadas
        classRepository.deleteById(classId); // Borra la clase
    }

    /**
     * Obtiene la lista de clases próximas (con fecha futura).
     *
     * @return Lista de clases próximas.
     */
    public List<Classes> findUpcomingClasses() {
        return classRepository.findByEventDateAfterOrderByEventDateAsc(java.time.LocalDate.now());
    }

    /**
     * Obtiene la lista de clases activas próximas junto con el número de plazas disponibles.
     *
     * @return Lista de DTOs con la clase y plazas restantes.
     */
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

    /**
     * Lista de combinaciones válidas de día, hora y nombre de clase.
     * Se utiliza para validar si una clase puede ser registrada en un horario específico.
     */
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

    /**
     * Valida si una combinación de día, hora y nombre de clase está permitida según el horario definido.
     *
     * @param day Día de la semana (en mayúsculas, ej: "LUNES").
     * @param time Hora en formato HH:mm.
     * @param className Nombre de la clase.
     * @return true si la combinación es válida, false en caso contrario.
     */
    public boolean isAllowedSchedule(String day, String time, String className) {
        return scheduleValid.stream()
            .anyMatch(ac -> ac.day().equals(day) && ac.time().equals(time) && ac.className().equals(className));
    }

    /**
     * Verifica si ya existe una clase registrada en una fecha y hora específicas.
     *
     * @param eventDate Fecha de la clase.
     * @param timeInit Hora de inicio de la clase.
     * @return true si existe una clase en esa fecha y hora, false en caso contrario.
     */
    public boolean existsByDateTime(LocalDate eventDate, LocalTime timeInit) {
        return classRepository.countByEventDateAndTimeInit(eventDate, timeInit) > 0;
    }

    /**
     * Verifica si ya existe una clase registrada en una fecha y hora específicas,
     * excluyendo una clase por su ID (útil para edición).
     *
     * @param eventDate Fecha de la clase.
     * @param timeInit Hora de inicio de la clase.
     * @param id ID de la clase a excluir.
     * @return true si existe otra clase en esa fecha y hora, false en caso contrario.
     */
    public boolean existsByDateTimeExcludingId(LocalDate eventDate, LocalTime timeInit, Integer id) {
        return classRepository.countByEventDateAndTimeInitAndIdNot(eventDate, timeInit, id) > 0;
    }
public int countReservationsForClass(Integer classId) {
    return reservationRepository.countByClassesId(classId);
}

}