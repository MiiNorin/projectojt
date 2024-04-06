package demo.service;
import demo.persistence.entity.Questions;
import demo.persistence.entity.TopicsEntity;
import demo.repository.QuestionRepository;
import demo.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public List<TopicsEntity> getAllTopics() {
        return topicRepository.findAll();
    }


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



    @Autowired
    private QuestionRepository questionRepository;



    public List<Questions> selectRandomQuestions() {
        return questionRepository.selectRandomQuestions();
    }

    public List<Questions> findRandomQuestionsByTopicId(Integer topicId, Integer totalQuestion) {
        return questionRepository.findRandomQuestionsByTopicId(topicId, totalQuestion);
    }

}