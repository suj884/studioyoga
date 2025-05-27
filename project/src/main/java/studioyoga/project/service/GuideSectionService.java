package studioyoga.project.service;

import studioyoga.project.model.GuideSection;
import studioyoga.project.repository.GuideSectionRepository;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Servicio para la gestión de secciones de la guía de yoga.
 * Proporciona métodos para obtener, guardar, listar y eliminar secciones de la guía.
 */
@Service
public class GuideSectionService {
    private final GuideSectionRepository repository;

    /**
     * Constructor que inyecta el repositorio de secciones de la guía.
     *
     * @param repository Repositorio de secciones de la guía.
     */
    public GuideSectionService(GuideSectionRepository repository) {
        this.repository = repository;
    }

    /**
     * Obtiene una sección de la guía por su ID.
     *
     * @param id ID de la sección.
     * @return La sección encontrada.
     * @throws java.util.NoSuchElementException si no se encuentra la sección.
     */
    public GuideSection getSectionById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    /**
     * Guarda o actualiza una sección de la guía.
     *
     * @param section Sección a guardar o actualizar.
     */
    public void saveSection(GuideSection section) {
        repository.save(section);
    }

    /**
     * Obtiene todas las secciones de la guía ordenadas por el campo sectionOrder.
     *
     * @return Lista de secciones ordenadas.
     */
    public List<GuideSection> getAllSections() {
        return repository.findAllByOrderBySectionOrderAsc();
    }

    /**
     * Elimina una sección de la guía por su ID.
     *
     * @param id ID de la sección a eliminar.
     */
    public void deleteSectionById(Long id) {
        repository.deleteById(id);
    }

}