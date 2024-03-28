package demo.repository;

import demo.persistence.entity.QuestiontestsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<QuestiontestsEntity, Integer> {
}