package WebApplication.Repositories;

import WebApplication.Entity.QuestiontestsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<QuestiontestsEntity, Integer> {
}
