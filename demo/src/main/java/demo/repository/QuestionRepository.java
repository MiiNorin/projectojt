package demo.repository;

import demo.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Questions, Integer> {
    List<Questions> findByQuestionContextContaining(String keyword);

    List<Questions> findByCreateDate(Date date);
}
