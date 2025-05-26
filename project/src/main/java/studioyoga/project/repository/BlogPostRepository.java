package studioyoga.project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import studioyoga.project.model.BlogPost;

/**
 * Repositorio para la entidad {@link BlogPost}.
 * Proporciona métodos para acceder y consultar publicaciones del blog en la
 * base de datos.
 */
public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {

    /**
     * Obtiene todas las publicaciones del blog ordenadas de forma descendente por
     * fecha de publicación.
     *
     * @return Lista de publicaciones ordenadas por fecha de publicación (más
     *         recientes primero).
     */
    List<BlogPost> findAllByOrderByPublishedDateDesc();
}