package studioyoga.project.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import studioyoga.project.model.User;

/**
 * Repositorio para la entidad {@link User}.
 * Proporciona métodos para acceder y consultar usuarios en la base de datos.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario a buscar.
     * @return Un {@link Optional} que contiene el usuario si existe, o vacío si no se encuentra.
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca usuarios cuyo primer apellido, segundo apellido o nombre contenga el texto proporcionado,
     * sin distinguir mayúsculas o minúsculas.
     *
     * @param firstLastName Texto a buscar en el primer apellido.
     * @param secondLastName Texto a buscar en el segundo apellido.
     * @param name Texto a buscar en el nombre.
     * @return Lista de usuarios que coinciden con alguno de los criterios.
     */
    List<User> findByFirstLastNameContainingIgnoreCaseOrSecondLastNameContainingIgnoreCaseOrNameContainingIgnoreCase(String firstLastName, String secondLastName, String name);

    /**
     * Busca usuarios por el nombre de su rol.
     *
     * @param roleName Nombre del rol.
     * @return Lista de usuarios que tienen el rol especificado.
     */
    List<User> findByRol_Name(String roleName);

}