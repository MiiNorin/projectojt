package demo.repository;


import demo.persistence.entity.TestDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestDetailRepository extends JpaRepository<TestDetailsEntity, Integer> {
    List<TestDetailsEntity> findByAccounts_UserId(Integer id);

}