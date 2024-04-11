package demo.repository;

import demo.persistence.entity.ChaptersEntity;
import demo.persistence.entity.SubjectsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ChapterRepository extends JpaRepository<ChaptersEntity, Integer> {
//    List<ChaptersEntity> findChaptersEntityBySubjectsSubjectId(Integer subjectId);

//    Page<ChaptersEntity> findBySubjectsSubjectId(Integer subjectId, Pageable pageable);
    Page<ChaptersEntity> findBySubjectsSubjectId(int subjectId, Pageable pageable);

    @Query("SELECT s.account.userId FROM SubjectsEntity s JOIN s.chaptersEntityList c WHERE c.chapterId = :chapterId AND s.subjectId = :subjectId")
    Integer findUserIdByChapterIdAndSubjectId(Integer chapterId, Integer subjectId);
}