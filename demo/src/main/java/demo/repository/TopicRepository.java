package demo.repository;

import demo.persistence.entity.TopicsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<TopicsEntity, Integer> {
}