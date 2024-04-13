package demo.repository;

import demo.persistence.entity.Account;
import demo.persistence.entity.Questions;
import demo.persistence.entity.SubjectsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<SubjectsEntity, Integer> {
    @Query("SELECT s.account.userId FROM SubjectsEntity s WHERE s.subjectId = :subjectId")
    Integer findAccountIdBySubjectId(Integer subjectId);
    List<SubjectsEntity> findSubjectsEntitiesByAccountUserId(Integer userId);

    Page<SubjectsEntity> findBySubjectNameContaining(String searchName, Pageable pageable);

    Page<SubjectsEntity> findByAccount_UserId(int userId, Pageable pageable);

    Page<SubjectsEntity> findAllByOrderByCreateDateDesc(Pageable pageable);

}