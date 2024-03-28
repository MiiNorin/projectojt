package demo.repository;

import demo.persistence.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Questions, Integer> {
    List<Questions> findByQuestionContextContaining(String keyword);

    List<Questions> findByCreateDateBetween(LocalDateTime startOfMonth, LocalDateTime endOfMonth);


    List<Questions> findAllByStatus(String status);
    List<Questions> findByTopicsEntity_TopicId(Integer topicId);

    @Query(value = "SELECT TOP (40) * FROM [Db_ZOTSystem].[dbo].[QUESTIONS] ORDER BY NEWID()", nativeQuery = true)
    List<Questions> selectRandomQuestions();

    @Query(value = "SELECT TOP 40 * FROM QUESTIONS WHERE topic_id = ?1 ORDER BY NEWID()", nativeQuery = true)
    List<Questions> findRandomQuestionsByTopicId(Integer topicId);
}
