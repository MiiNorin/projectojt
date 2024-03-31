package WebApplication.Repositories;

import WebApplication.Entity.QuestiontestsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionTestRepository extends JpaRepository<QuestiontestsEntity, Integer> {
}
