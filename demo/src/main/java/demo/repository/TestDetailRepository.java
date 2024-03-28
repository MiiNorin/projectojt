package demo.repository;


import demo.persistence.entity.TestDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDetailRepository extends JpaRepository<TestDetailsEntity, Integer> {
}