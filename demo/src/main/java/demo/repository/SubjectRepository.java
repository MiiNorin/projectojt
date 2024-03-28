package demo.repository;

import demo.persistence.entity.SubjectsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<SubjectsEntity, Integer> {
}