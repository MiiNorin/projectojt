package demo.repository;

import demo.persistence.entity.ChaptersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ChapterRepository extends JpaRepository<ChaptersEntity, Integer> {
    List<ChaptersEntity> findChaptersEntityBySubjectsSubjectId(Integer subjectId);
    @Query("SELECT s.account.userId FROM SubjectsEntity s JOIN s.chaptersEntityList c WHERE c.chapterId = :chapterId AND s.subjectId = :subjectId")
    Integer findUserIdByChapterIdAndSubjectId(Integer chapterId, Integer subjectId);
}