package WebApplication.Services;

import WebApplication.Entity.CommentEntity;
import WebApplication.Entity.TopicsEntity;
import WebApplication.Repositories.CommentRepository;
import WebApplication.Repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public List<CommentEntity> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<CommentEntity> getCommentById(int id) {
        return commentRepository.findById(id);
    }

    public CommentEntity saveComment(CommentEntity comment) {
        return commentRepository.save(comment);
    }

    public void deleteCommentById(int id) {
        commentRepository.deleteById(id);
    }

    public CommentEntity updateComment(CommentEntity updatedComment) {
        // Kiểm tra xem entity có tồn tại trong database không
        Optional<CommentEntity> existingComment = commentRepository.findById(updatedComment.getCommentId());

        if (existingComment.isPresent()) {
            CommentEntity commentToUpdate = existingComment.get();
            commentToUpdate.setCommentcontext(updatedComment.getCommentcontext());
            return commentRepository.save(commentToUpdate);
        } else {
            return null;
        }
    }

}
