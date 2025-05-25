package studioyoga.project.controller;


import org.springframework.stereotype.Service;
import studioyoga.project.model.Rol;
import studioyoga.project.repository.RolRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    public Optional<Rol> findByName(String name) {
        return rolRepository.findByName(name);
    }
}
