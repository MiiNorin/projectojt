package demo.controller;


import demo.persistence.entity.TestDetailsEntity;
import demo.repository.TestDetailRepository;
import demo.service.QuestionTestService;
import demo.persistence.entity.Questions;
import demo.persistence.entity.QuestiontestsEntity;
import demo.repository.AnswerRepository;
import demo.repository.QuestionRepository;
import demo.repository.QuestionTestRepository;
import demo.service.QuestionService;
import demo.service.TestDetailService;
import demo.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.*;

@RequestMapping("/questiontests")
@Controller
public class QuestionTestController {

    @Autowired
    private QuestionTestService questionTestService;
    @Autowired
    private TestDetailRepository testDetailRepository;
    @Autowired
    private QuestionService questionService;

    @Autowired
    private TopicService topicService;
    @Autowired
    private QuestionTestRepository questionTestRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @GetMapping("/{testDetailId}")
    public String showQuestionTests(Model model, @PathVariable("testDetailId") int testDetailId) {
        List<QuestiontestsEntity> questionTests = questionTestService.getQuestionTestsByTestDetailId(testDetailId);
        List<Map<String, Object>> questionInfoList = new ArrayList<>();
        for (QuestiontestsEntity questionTest : questionTests) {
            Map<String, Object> questionInfo = new HashMap<>();
            Questions question = questionRepository.findById(questionTest.getQuestionId()).orElse(null);
            if (question != null && questionTest.getQuestionId() == question.getQuestionId()) {
                String choice="";
                String option="";
                String solution="";
                int correctAnswer = question.getAnswerId();
                switch (correctAnswer){
                    case 1:
                        option = question.getOptionA();
                        questionInfo.put("option", option);
                        break;
                    case 2:
                        option = question.getOptionB();
                        questionInfo.put("option", option);
                        break;
                    case 3:
                        option = question.getOptionC();
                        questionInfo.put("option", option);
                        break;
                    case 4:
                        option = question.getOptionD();
                        questionInfo.put("option", option);
                        break;
                    default:
                        questionInfo.put("option", "Bỏ trống");
                        break;
                }
                int userAnswer = questionTest.getAnswerId();
                switch (userAnswer){
                    case 1:
                        choice = question.getOptionA();
                        questionInfo.put("choice", choice);
                        break;
                    case 2:
                        choice = question.getOptionB();
                        questionInfo.put("choice", choice);
                        break;
                    case 3:
                        choice = question.getOptionC();
                        questionInfo.put("choice", choice);
                        break;
                    case 4:
                        choice = question.getOptionD();
                        questionInfo.put("choice", choice);
                        break;
                    default:
                        questionInfo.put("choice", "Bỏ trống");
                        break;
                }
                String questionContext = question.getQuestionContext();
                String questionSolution = question.getSolution();
                System.out.println(questionContext);
                questionInfo.put("questionContext", questionContext);
                questionInfo.put("correctAnswer", correctAnswer);
                questionInfo.put("userAnswer", userAnswer);
                questionInfo.put("questionSolution", questionSolution);
                // Thêm map này vào danh sách
                questionInfoList.add(questionInfo);
            }
        }

        // Thêm danh sách thông tin câu hỏi vào model
        model.addAttribute("questionInfoList", questionInfoList);

        return "questionTests";
    }


    @Autowired
    private TestDetailService testDetailService;

    @PostMapping("/submitAnswers")
    public String submitAnswers(@RequestParam("questionId") String[] questionIds,
                                @RequestParam("selectedAnswer") String selectedAnswers,
                                @RequestParam("testDetailId") Integer testDetailId) {

        String[] selectedAnswersArray = selectedAnswers.split(",");
        TestDetailsEntity testDetailsEntity = testDetailRepository.findById(testDetailId).orElse(null);
        Map<Integer, Integer> questionAnswerMap = new HashMap<>();

        for (int i = 0; i < questionIds.length; i++) {
            Integer questionId = Integer.parseInt(questionIds[i]);
            Integer selectedAnswer = i < selectedAnswersArray.length ?
                    Integer.parseInt(selectedAnswersArray[i]) : 5;
            questionAnswerMap.put(questionId, selectedAnswer);
        }

        for (Map.Entry<Integer, Integer> entry : questionAnswerMap.entrySet()) {
            Integer questionId = entry.getKey();
            Integer selectedAnswer = entry.getValue();
            questionTestService.saveAnswer(questionId, selectedAnswer, testDetailId);
        }

        List<QuestiontestsEntity> questionTests = questionTestService.getQuestionTestsByTestDetailId(testDetailId);
        List<Map<String, Object>> questionInfoList = new ArrayList<>();
        double scoreCount = 0;
        double totalQuestion = 0;
        double totalCorrectQuestion =0;
        for (QuestiontestsEntity questionTest : questionTests) {
            Map<String, Object> questionInfo = new HashMap<>();
            Questions question = questionRepository.findById(questionTest.getQuestionId()).orElse(null);
            totalQuestion++;
            if (question != null && questionTest.getQuestionId() == question.getQuestionId()) {
                int correctAnswer = question.getAnswerId();
                int userAnswer = questionTest.getAnswerId();
                if (correctAnswer==userAnswer){
                    totalCorrectQuestion++;
                }
            }
        }
        scoreCount = totalCorrectQuestion/totalQuestion*10;
        testDetailService.updateSubmittedStatus(testDetailId, scoreCount);
        return "result";
    }

}