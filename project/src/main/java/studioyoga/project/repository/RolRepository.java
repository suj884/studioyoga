package studioyoga.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import studioyoga.project.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByName(String name);

}
