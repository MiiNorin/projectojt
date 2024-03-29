package demo.repository;

import demo.persistence.entity.TopicsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<TopicsEntity, Integer> {
    List<TopicsEntity> findTopicsEntitiesByChapterChapterId(Integer chapterId);
}