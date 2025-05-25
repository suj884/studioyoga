package studioyoga.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import studioyoga.project.model.BlogPost;
import studioyoga.project.repository.BlogPostRepository;

@Service
public class BlogService {
     @Autowired
    private BlogPostRepository blogPostRepository;

    // Obtener todos los posts
    public List<BlogPost> findAll() {
        return blogPostRepository.findAll();
    }

    // Obtener post por id
    public Optional<BlogPost> findById(Integer id) {
        return blogPostRepository.findById(id);
    }

    // Guardar o actualizar post
    public BlogPost save(BlogPost post) {
        return blogPostRepository.save(post);
    }

    // Eliminar post
    public void deleteById(Integer id) {
        blogPostRepository.deleteById(id);
    }

}
