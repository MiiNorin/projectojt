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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TestDetailController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestDetailRepository testDetailRepository;

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

    @GetMapping("/markreport")
    public String redirectToSearchPage() {
        return "markreport";
    }

    @GetMapping("/markreport/search")
    public String searchByEmail(@RequestParam("email") String email, Model model, HttpSession session) {
        Integer loggedInUserId = (Integer) session.getAttribute("user_id");
        Account account = accountRepository.findByEmail(email).orElse(null);

        if (account != null && loggedInUserId != null && loggedInUserId.equals(account.getUserId())) {
            model.addAttribute("account", account);

            List<TestDetailsEntity> testDetailsList = testDetailRepository.findByAccounts_UserId(account.getUserId());
            if (!testDetailsList.isEmpty()) {
                model.addAttribute("testDetailsList", testDetailsList);

                for (TestDetailsEntity testDetail : testDetailsList) {
                    int testDetailId = testDetail.getTestDetailid();

                    List<QuestiontestsEntity> questionTests = questionTestService.getQuestionTestsByTestDetailId(testDetailId);
                    int correctCount = 0;
                    int totalQuestion = 0;
                    Integer topicId = null;

                    if (!questionTests.isEmpty()) {
                        QuestiontestsEntity firstQuestionTest = questionTests.get(0);
                        Questions question = questionRepository.findById(firstQuestionTest.getQuestionId()).orElse(null);
                        if (question != null) {
                            topicId = question.getTopics().getTopicId();
                        }
                    }

                    if (topicId != null) {
                        totalQuestion = topicService.getTotalQuestionByTopicId(topicId);
                    }

                    for (QuestiontestsEntity questionTest : questionTests) {
                        Questions question = questionRepository.findById(questionTest.getQuestionId()).orElse(null);
                        if (question != null && question.getAnswerId() != null && question.getAnswerId().equals(questionTest.getAnswerId())) {
                            correctCount++;
                        }
                    }

//                    double score = totalQuestion != 0 ? ((double) correctCount / totalQuestion) * 10 : 0;
//                    testDetail.setScore(score);
                    testDetail.setAccounts(account);
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
