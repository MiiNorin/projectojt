package demo.repository;

import demo.persistence.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Questions, Integer> {
    List<Questions> findByQuestionContextContaining(String keyword);

    List<Questions> findByCreateDate(Date date);

    List<Questions> findAllByStatus(String status);
}
