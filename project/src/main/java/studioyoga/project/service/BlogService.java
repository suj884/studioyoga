package studioyoga.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import studioyoga.project.model.BlogPost;
import studioyoga.project.repository.BlogPostRepository;

/**
 * Servicio para la gestión de publicaciones del blog.
 * Proporciona métodos para listar, buscar, guardar, actualizar y eliminar entradas del blog.
 */
@Service
public class BlogService {
    @Autowired
    private BlogPostRepository blogPostRepository;

    /**
     * Obtiene todas las publicaciones del blog.
     *
     * @return Lista de todas las publicaciones.
     */
    public List<BlogPost> findAll() {
        return blogPostRepository.findAll();
    }

    /**
     * Obtiene todas las publicaciones del blog ordenadas por fecha de publicación descendente.
     *
     * @return Lista de publicaciones ordenadas por fecha (más recientes primero).
     */
    public List<BlogPost> findAllPublishedOrdered() {
        return blogPostRepository.findAllByOrderByPublishedDateDesc();
    }

    /**
     * Busca una publicación del blog por su ID.
     *
     * @param id ID de la publicación.
     * @return Un {@link Optional} que contiene la publicación si existe, o vacío si no se encuentra.
     */
    public Optional<BlogPost> findById(Integer id) {
        return blogPostRepository.findById(id);
    }

    /**
     * Guarda o actualiza una publicación del blog.
     *
     * @param post Publicación a guardar o actualizar.
     * @return La publicación guardada o actualizada.
     */
    public BlogPost save(BlogPost post) {
        return blogPostRepository.save(post);
    }

    /**
     * Elimina una publicación del blog por su ID.
     *
     * @param id ID de la publicación a eliminar.
     */
    public void deleteById(Integer id) {
        blogPostRepository.deleteById(id);
    }

}