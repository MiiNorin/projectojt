package demo.repository;

import demo.persistence.entity.AnswersEntity;
import demo.persistence.entity.ChaptersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<AnswersEntity, Integer> {
    @Query("SELECT a FROM AnswersEntity a WHERE a.answerId = :answer_id")
    AnswersEntity findById(@Param("answer_id") int answer_id);
}
