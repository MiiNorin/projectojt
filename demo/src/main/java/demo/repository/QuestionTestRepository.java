package demo.repository;

import demo.persistence.entity.QuestiontestsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTestRepository extends JpaRepository<QuestiontestsEntity, Integer>{
    List<QuestiontestsEntity> findQuestionTestsEntitiesByTestDetailIdAndQuestionId(int testDetailId, int questionId);

    List<QuestiontestsEntity> findByTestDetailId(int testDetailId);
}
