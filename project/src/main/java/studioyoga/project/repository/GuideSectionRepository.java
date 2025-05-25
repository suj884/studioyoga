package studioyoga.project.repository;

import studioyoga.project.model.GuideSection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GuideSectionRepository extends JpaRepository<GuideSection, Long> {
    List<GuideSection> findAllByOrderBySectionOrderAsc();
}
