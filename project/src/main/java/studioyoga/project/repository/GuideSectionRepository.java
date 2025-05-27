package studioyoga.project.repository;

import studioyoga.project.model.GuideSection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio para la entidad {@link GuideSection}.
 * Proporciona métodos para acceder y consultar secciones de la guía en la base de datos.
 */
public interface GuideSectionRepository extends JpaRepository<GuideSection, Long> {

    /**
     * Obtiene todas las secciones de la guía ordenadas de forma ascendente por el campo de orden.
     *
     * @return Lista de secciones de la guía ordenadas por el campo de orden.
     */
    List<GuideSection> findAllByOrderBySectionOrderAsc();

    /**
     * Busca una sección de la guía por su identificador.
     *
     * @param id Identificador de la sección de la guía.
     * @return Sección de la guía con el identificador especificado, o {@code null} si no se encuentra.
     */
    GuideSection findById(long id);

}