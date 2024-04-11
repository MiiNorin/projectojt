package demo.service;
import demo.persistence.entity.Questions;
import demo.persistence.entity.TopicsEntity;
import demo.repository.QuestionRepository;
import demo.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public List<TopicsEntity> getAllTopics() {

        return topicRepository.findAll();
    }

    @Autowired
    private QuestionRepository questionRepository;
    public void deleteTopicById(int id) {
        topicRepository.deleteById(id);
    }
    public int countTotalQuestionsByChapterId(int chapterId) {
        Iterable<TopicsEntity> topics = topicRepository.findTopicsEntitiesByChapterChapterId(chapterId);
        int totalQuestions = 0;
        for (TopicsEntity topic : topics) {
            totalQuestions += topic.getTotalQuestion();
        }

        return totalQuestions;
    }



    public List<Questions> selectRandomQuestions() {
        return questionRepository.selectRandomQuestions();
    }

    public List<Questions> findRandomQuestionsByTopicId(Integer topicId, Integer totalQuestion) {
        return questionRepository.findRandomEasyQuestionsByTopicId(topicId, totalQuestion);
    }
    public void createTopicWithQuestions(int topicId, int totalQuestion, int totalHardQuestions) {
        List<Questions> hardQuestions = selectRandomQuestions();
        int remainingQuestions = totalQuestion - totalHardQuestions;
        int actualHardQuestions = Math.min(totalHardQuestions, hardQuestions.size());
        List<Questions> selectedHardQuestions = hardQuestions.subList(0, actualHardQuestions);

        List<Questions> remainingEasyQuestions = new ArrayList<>();

        List<Questions> allQuestions = new ArrayList<>();
        allQuestions.addAll(selectedHardQuestions);
        allQuestions.addAll(remainingEasyQuestions);

    }

    public Integer getTotalQuestionByTopicId(Integer id) {
        Optional<TopicsEntity> topicOptional = topicRepository.findById(id);
        if (topicOptional.isPresent()) {
            TopicsEntity topic = topicOptional.get();
            return topic.getTotalQuestion();
        } else {
            return null;
        }
    }

    public TopicsEntity getTopicEntity(int topic_id) {

        return topicRepository.findTopicsEntityByTopicId(topic_id);
    }

}