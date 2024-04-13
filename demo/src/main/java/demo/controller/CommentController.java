package demo.controller;

import demo.persistence.entity.CommentEntity;
import demo.service.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@RequestMapping("/listComments")
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/listTest/{subjectId}/{chapterId}")
    public String getAllComments(@PathVariable("chapterId") String chapterId, Model model) {
        List<CommentEntity> comments = commentService.getCommentsByChapterId(Integer.parseInt(chapterId));
        model.addAttribute("comments", comments);
        return "showListTestForStudent"; // Trả về view để hiển thị các comment
    }

    // Thêm một comment mới
    @PostMapping("/listTest/{subjectId}/{chapterId}/comments")
    public String addComment(@PathVariable("chapterId") int chapterId,
                             @PathVariable("subjectId") int subjectId,
                             @RequestParam("commentcontext") String commentText,
                             HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        CommentEntity comment = new CommentEntity();
        comment.setChapterId(chapterId);
        comment.setCommentContext(commentText);
        comment.setAccountId(userId);
        commentService.saveComment(comment);
        return "redirect:/listTopics/listTest/" + subjectId +'/'+ chapterId; // Redirect lại trang danh sách comment
    }

    // Cập nhật một comment
    @PostMapping("/listTest/{subjectId}/{chapterId}/comments/{commentId}")
    public String updateComment(@PathVariable("chapterId") int chapterId,
                                @PathVariable("commentId") int commentId,
                                @RequestParam("commentcontext") String commentText) {
        CommentEntity comment = commentService.getCommentById(commentId).orElse(null);
        if (comment != null) {
            comment.setCommentContext(commentText);
            commentService.saveComment(comment);
        }
        return "redirect:/listTopics/listTest/{subjectId}/{chapterId}"; // Redirect lại trang danh sách comment
    }

    // Xóa một comment
    @PostMapping("/listTest/{subjectId}/{chapterId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("chapterId") int chapterId,
                                @PathVariable("commentId") int commentId) {
        commentService.deleteCommentById(commentId);
        return "redirect:/listTopics/listTest/{subjectId}/{chapterId}"; // Redirect lại trang danh sách comment
    }


}
