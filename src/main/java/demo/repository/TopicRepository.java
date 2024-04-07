package demo.repository;


import demo.entity.Topics;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.jms.Topic;

public interface TopicRepository extends JpaRepository<Topics, Long> {
}
