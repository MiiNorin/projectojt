package WebApplication.Repositories;

import WebApplication.Entity.QuestionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionsEntity, Integer> {
    List<QuestionsEntity> findByTopicId(Integer topicId);

    @Query(value = "SELECT TOP (40) * FROM [Db_ZOTSystem].[dbo].[QUESTIONS] ORDER BY NEWID()", nativeQuery = true)
    List<QuestionsEntity> selectRandomQuestions();

    @Query(value = "SELECT TOP 40 * FROM QUESTIONS WHERE topic_id = ?1 ORDER BY NEWID()", nativeQuery = true)
    List<QuestionsEntity> findRandomQuestionsByTopicId(Integer topicId);

}
