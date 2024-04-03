package demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class CoursesController {
    @GetMapping("/Course")
    String course() {
        return "course";
    }
}
