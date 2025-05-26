package studioyoga.project.service;

import org.springframework.stereotype.Service;
import studioyoga.project.model.Rol;
import studioyoga.project.repository.RolRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de roles de usuario.
 * Proporciona métodos para listar todos los roles y buscar roles por nombre.
 */
@Service
public class RolService {

    private final RolRepository rolRepository;

    /**
     * Constructor que inyecta el repositorio de roles.
     *
     * @param rolRepository Repositorio de roles.
     */
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    /**
     * Obtiene la lista de todos los roles registrados.
     *
     * @return Lista de roles.
     */
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    /**
     * Busca un rol por su nombre.
     *
     * @param name Nombre del rol.
     * @return Un {@link Optional} con el rol si existe, o vacío si no se encuentra.
     */
    public Optional<Rol> findByName(String name) {
        return rolRepository.findByName(name);
    }
}