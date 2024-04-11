package demo.controller;

import demo.persistence.entity.Account;
import demo.persistence.entity.Questions;
import demo.persistence.entity.QuestiontestsEntity;
import demo.persistence.entity.TestDetailsEntity;
import demo.repository.AccountRepository;
import demo.repository.QuestionRepository;
import demo.repository.TestDetailRepository;
import demo.repository.TopicRepository;
import demo.service.QuestionTestService;
import demo.service.TestDetailService;
import demo.service.TopicService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Controller
public class TestDetailController {

    @Autowired
    private AccountRepository accountRepository;


    @GetMapping("/markreport")
    public String redirectToSearchPage() {
        return "markreport";
    }


    @Autowired
    private TestDetailRepository testDetailsRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private QuestionTestService questionTestService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TestDetailService testDetailService;

    @GetMapping("/markreport/search")
    public String searchByEmail(@RequestParam("email") String email, Model model) {
        Account a = accountRepository.findByEmail(email).orElse(null);
        if (a != null) {
            model.addAttribute("account", a);

            // Lấy tất cả các chi tiết kiểm tra dựa trên ID tài khoản
            List<TestDetailsEntity> testDetailsList = testDetailsRepository.findByAccountId(a.getUserId());
            if (!testDetailsList.isEmpty()) {
                model.addAttribute("testDetailsList", testDetailsList);

                // Lặp qua danh sách các chi tiết kiểm tra
                for (TestDetailsEntity testDetail : testDetailsList) {
                    int testDetailId = testDetail.getTestDetailid();

                    List<QuestiontestsEntity> questionTests = questionTestService.getQuestionTestsByTestDetailId(testDetailId);

                    int correctCount = 0;

                    Integer topicId = null;
                    if (!questionTests.isEmpty()) {
                        QuestiontestsEntity firstQuestionTest = questionTests.get(0);
                        Questions question = questionRepository.findById(firstQuestionTest.getQuestionId()).orElse(null);
                        if (question != null) {
                            topicId = question.getTopics().getTopicId();
                        }
                    }

                    int totalQuestion = 0;
                    if (topicId != null) {
                        totalQuestion = topicService.getTotalQuestionByTopicId(topicId);
                    }

                    for (QuestiontestsEntity questionTest : questionTests) {
                        Questions question = questionRepository.findById(questionTest.getQuestionId()).orElse(null);
                        if (question != null) {
                            if (question.getAnswerId() != null && question.getAnswerId().equals(questionTest.getAnswerId())) {
                                correctCount++;
                            }
                        }
                    }

                    double score = 0;
                    if (totalQuestion != 0) {
                        score = ((double) correctCount / totalQuestion) * 10;
                    }

                    // In ra điểm ra console
                    System.out.println("Số câu đúng: " + correctCount);
                    System.out.println("Tổng số câu hỏi: " + totalQuestion);
                    System.out.println("Điểm: " + score);

                    // Cập nhật điểm vào chi tiết kiểm tra và lưu vào cơ sở dữ liệu
                    testDetail.setScore(score);
                    testDetailService.saveTestDetail(testDetail);
                }
            } else {
                model.addAttribute("errorMessage", "Không tìm thấy chi tiết kiểm tra cho tài khoản có email: " + email);
            }
        } else {
            model.addAttribute("errorMessage", "Không tìm thấy tài khoản cho email: " + email);
        }
        model.addAttribute("topicRepository", topicRepository);

        return "mark_report";
    }

}
