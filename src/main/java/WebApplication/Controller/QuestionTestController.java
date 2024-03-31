package WebApplication.Controller;

import WebApplication.Services.QuestionTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuestionTestController {

    @Autowired
    private QuestionTestService questionTestService;

    @PostMapping("/submitAnswers")
    public String submitAnswers(@RequestParam("questionId") String[] questionIds,
                                @RequestParam("selectedAnswer") String selectedAnswers) {

        // Phân tách chuỗi selectedAnswers thành một mảng các giá trị
        String[] selectedAnswersArray = selectedAnswers.split(",");

        for (int i = 0; i < questionIds.length; i++) {
            Integer questionId = Integer.parseInt(questionIds[i]);
            Integer selectedAnswer = 0;

            // Kiểm tra xem selectedAnswersArray có đủ phần tử hay không
            if (selectedAnswersArray.length > i) {
                // Chuyển đổi chuỗi con thành số nguyên
                try {
                    selectedAnswer = Integer.parseInt(selectedAnswersArray[i]);
                } catch (NumberFormatException e) {
                    // Xử lý lỗi khi không thể chuyển đổi thành số nguyên
                    System.err.println("Invalid selected answer format at index " + i + ": " + selectedAnswersArray[i]);
                }
            }

            System.out.println("Question ID: " + questionId + ", Selected Answer: " + selectedAnswer);
            questionTestService.saveAnswer(questionId, selectedAnswer);
        }

        return "redirect:/result";
    }

}
