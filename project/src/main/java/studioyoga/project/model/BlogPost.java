package studioyoga.project.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una publicación del blog.
 * Contiene información como el título, contenido, resumen, imagen, fecha de publicación y un identificador único.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blog_posts")
@Entity
public class BlogPost {
    /**
     * Identificador único de la publicación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Título de la publicación.
     */
    private String title;

    /**
     * Contenido completo de la publicación.
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * Resumen de la publicación.
     */
    @Column(columnDefinition = "TEXT")
    private String summary;

    /**
     * URL de la imagen asociada a la publicación.
     */
    private String imageUrl;

    /**
     * Fecha de publicación de la entrada del blog.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishedDate;
}