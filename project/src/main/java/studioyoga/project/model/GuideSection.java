package studioyoga.project.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa una sección de la guía de yoga.
 * Contiene información como el título, contenido (puede incluir HTML), imagen asociada y el orden de la sección.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "guide_sections")
public class GuideSection {
    /**
     * Identificador único de la sección.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título de la sección de la guía.
     */
    private String title;

    /**
     * Contenido de la sección (puede contener HTML).
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * URL de la imagen asociada a la sección (opcional).
     */
    private String imageUrl;

    /**
     * Orden de la sección dentro de la guía.
     */
    private Integer sectionOrder;
}