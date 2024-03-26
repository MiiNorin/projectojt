package WebApplication.Controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;


@RequestMapping("/home")
@Controller
public class HomeController {
    @GetMapping("/manageQuestion")
    public String moveToShowQuestion(){
        return "mainPage";
    }

}