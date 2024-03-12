package WebApplication.Repositories;

import WebApplication.Entity.SubjectsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<SubjectsEntity, Integer> {
}
