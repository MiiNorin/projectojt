package demo.repository;

import demo.persistence.entity.Questions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Questions, Integer> {
    List<Questions> findByQuestionContextContaining(String keyword);

    int countByChaptersChapterId(long chapterId);
    List<Questions> findQuestionsByChaptersChapterId(Integer chapterId);

    @Query(value = "SELECT TOP (40) * FROM [Db_ZOTSystem].[dbo].[QUESTIONS] ORDER BY NEWID()", nativeQuery = true)
    List<Questions> selectRandomQuestions();

    @Query(value = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER (ORDER BY NEWID()) AS RowNum FROM QUESTIONS WHERE topic_id = ?1) AS SubQuery WHERE RowNum <= ?2", nativeQuery = true)
    List<Questions> findRandomQuestionsByTopicId(Integer topicId, Integer topN);

    @Query(value = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER (ORDER BY NEWID()) AS RowNum FROM QUESTIONS WHERE chapter_id = ?1) AS SubQuery WHERE RowNum <= ?2", nativeQuery = true)
    List<Questions> findRandomQuestionsByChapterId(Integer topicId, Integer topN);
    Page<Questions> findByQuestionContextContainingAndSubjectSubjectIdAndChaptersChapterId(String name, int subjectId, int chapterId, Pageable pageable);

    Page<Questions> findByQuestionContextContainingAndSubjectSubjectId(String name, int subjectId, Pageable pageable);
    Page<Questions> findByChaptersChapterId(Integer chapterId, Pageable pageable);
    Page<Questions> findBySubjectSubjectId(Integer subjectId, Pageable pageable);

    long countByTopicsTopicIdAndChaptersChapterId(int topicId, Integer chapterId);
}
