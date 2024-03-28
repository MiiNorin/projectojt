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

    public Optional<TopicsEntity> getTopicById(int id) {
        return topicRepository.findById(id);
    }

    public TopicsEntity saveTopic(TopicsEntity topic) {
        return topicRepository.save(topic);
    }

    public void deleteTopicById(int id) {
        topicRepository.deleteById(id);
    }

    public TopicsEntity updateTopic(TopicsEntity updatedTopic) {
        Optional<TopicsEntity> existingTopic = topicRepository.findById(updatedTopic.getTopicId());

        if (existingTopic.isPresent()) {
            // Nếu tồn tại, cập nhật thông tin và lưu lại
            TopicsEntity topicToUpdate = existingTopic.get();
            topicToUpdate.setTopicName(updatedTopic.getTopicName());
            topicToUpdate.setDuration(updatedTopic.getDuration());
            topicToUpdate.setTotalQuestion(updatedTopic.getTotalQuestion());
            topicToUpdate.setTopicType(updatedTopic.getTopicType());
            topicToUpdate.setGrade(updatedTopic.getGrade());
            topicToUpdate.setCreateDate(updatedTopic.getCreateDate());
            topicToUpdate.setStatus(updatedTopic.getStatus());
            topicToUpdate.setStartTestDate(updatedTopic.getStartTestDate());
            topicToUpdate.setFinishTestDate(updatedTopic.getFinishTestDate());
            topicToUpdate.setSubjectId(updatedTopic.getSubjectId());
            return topicRepository.save(topicToUpdate);
        } else {
            return null;
        }
    }


    @Autowired
    private QuestionRepository questionRepository;

    public List<Questions> getQuestionsByTopicId(Integer topicId) {
        return questionRepository.findByTopicsEntity_TopicId(topicId);
    }

    public List<Questions> selectRandomQuestions() {
        return questionRepository.selectRandomQuestions();
    }

    public List<Questions> getRandomQuestionsByTopicId(Integer topicId) {
        return questionRepository.findRandomQuestionsByTopicId(topicId);
    }
}