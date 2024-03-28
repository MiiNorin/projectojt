package demo.repository;

import demo.persistence.entity.ChaptersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<ChaptersEntity, Integer> {
}
