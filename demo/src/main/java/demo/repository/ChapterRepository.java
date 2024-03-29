package demo.repository;

import demo.persistence.entity.ChaptersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChapterRepository extends JpaRepository<ChaptersEntity, Integer> {
    List<ChaptersEntity> findChaptersEntityBySubjectsSubjectId(Integer subjectId);

}