package WebApplication.Controller;

import WebApplication.Entity.CommentEntity;
import WebApplication.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/comments")
    public String getAllComments(Model model) {
        List<CommentEntity> comments = commentService.getAllComments();
        model.addAttribute("comments", comments);
        return "Comment";
    }

    @GetMapping("/comments/{id}")
    public String getCommentById(@PathVariable("id") int id, Model model) {
        Optional<CommentEntity> optionalComment = commentService.getCommentById(id);
        optionalComment.ifPresent(comment -> model.addAttribute("comment", comment));
        return "Comment";
    }

    @GetMapping("/comments/new")
    public String showCommentForm(Model model) {
        model.addAttribute("comment", new CommentEntity());
        return "Comment";
    }

    @PostMapping("/comments")
    public String createComment(@RequestParam("commentcontext") String commentText) {
        CommentEntity comment = new CommentEntity();
        comment.setCommentcontext(commentText);
        commentService.saveComment(comment);
        return "redirect:/comments";
    }


    @GetMapping("/comments/{id}/edit")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Optional<CommentEntity> optionalComment = commentService.getCommentById(id);
        optionalComment.ifPresent(comment -> model.addAttribute("comment", comment));
        return "edit_form";
    }

    @PostMapping("/comments/{id}/edit")
    public String updateComment(@PathVariable("id") int id, @RequestParam("commentcontext") String commentContext) {
        Optional<CommentEntity> optionalComment = commentService.getCommentById(id);
        if (optionalComment.isPresent()) {
            CommentEntity comment = optionalComment.get();
            comment.setCommentcontext(commentContext);
            commentService.updateComment(comment);
        }
        return "redirect:/comments";
    }


    @GetMapping("/comments/{id}/delete")
    public String deleteComment(@PathVariable("id") int id) {
        commentService.deleteCommentById(id);
        return "redirect:/comments";
    }
}
