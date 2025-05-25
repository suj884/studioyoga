package studioyoga.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import studioyoga.project.model.Rol;

/**
 * Repositorio para la entidad {@link Rol}.
 * Proporciona métodos para acceder y consultar roles en la base de datos.
 */
public interface RolRepository extends JpaRepository<Rol, Integer> {

    /**
     * Busca un rol por su nombre.
     *
     * @param name Nombre del rol a buscar.
     * @return Un {@link Optional} que contiene el rol si existe, o vacío si no se encuentra.
     */
    Optional<Rol> findByName(String name);

}