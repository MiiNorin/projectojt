package demo.repository;

import demo.persistence.entity.Questions;
import demo.persistence.entity.SubjectsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<SubjectsEntity, Integer> {

}