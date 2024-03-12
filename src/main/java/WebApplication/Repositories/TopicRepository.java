package WebApplication.Repositories;

import WebApplication.Entity.TopicsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<TopicsEntity, Integer> {
}
