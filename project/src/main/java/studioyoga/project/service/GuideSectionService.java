package studioyoga.project.service;


import studioyoga.project.model.GuideSection;
import studioyoga.project.repository.GuideSectionRepository;

import java.util.List;

import org.springframework.stereotype.Service;


@Service
public class GuideSectionService {
	  private final GuideSectionRepository repository;

	    public GuideSectionService(GuideSectionRepository repository) {
	        this.repository = repository;
	    }

	    public GuideSection getSectionById(Long id) {
	        return repository.findById(id).orElseThrow();
	    }

	    public void saveSection(GuideSection section) {
	        repository.save(section);
	    }

	    public List<GuideSection> getAllSections() {
	        return repository.findAllByOrderBySectionOrderAsc();
	    }
	    public void deleteSectionById(Long id) {
	        repository.deleteById(id);
	    }


}
