package demo.repository;

import demo.persistence.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findByChapterId(Integer chapterId);
    List<CommentEntity> findByAccountId(Integer accountId);
}