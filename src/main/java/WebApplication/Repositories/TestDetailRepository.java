package WebApplication.Repositories;

import WebApplication.Entity.TestDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDetailRepository extends JpaRepository<TestDetailsEntity, Integer> {
}
