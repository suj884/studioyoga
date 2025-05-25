package studioyoga.project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import studioyoga.project.model.BlogPost;

public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
    List<BlogPost> findAllByOrderByPublishedDateDesc();

   

}
