package WebApplication.Repositories;

import WebApplication.Entity.QuestionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionsEntity, Integer> {
    List<QuestionsEntity> findByTopicId(Integer topicId);

//    @Query(value = "SELECT TOP ?1 * FROM QUESTIONS WHERE topic_id = ?2 ORDER BY NEWID()", nativeQuery = true)
//    List<QuestionsEntity> findRandomQuestionsByTopicId(Integer totalQuestion,Integer topicId);

    @Query(value = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER (ORDER BY NEWID()) AS RowNum FROM QUESTIONS WHERE topic_id = ?1) AS SubQuery WHERE RowNum <= ?2", nativeQuery = true)
    List<QuestionsEntity> findRandomQuestionsByTopicId(Integer topicId, Integer topN);

    List<QuestionsEntity> findByQuestionContextContaining(String keyword);

    List<QuestionsEntity> findByCreateDate(Date date);

    List<QuestionsEntity> findAllByStatus(String status);

}
