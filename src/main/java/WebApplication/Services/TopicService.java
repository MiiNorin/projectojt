package WebApplication.Services;

import WebApplication.Entity.QuestionsEntity;
import WebApplication.Entity.TopicsEntity;
import WebApplication.Repositories.QuestionRepository;
import WebApplication.Repositories.TopicRepository;
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
        // Kiểm tra xem entity có tồn tại trong database không
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
            // Nếu không tồn tại, có thể xử lý tùy ý, ví dụ ném một exception hoặc trả về null
            return null;
        }
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


    @Autowired
    private QuestionRepository questionRepository;

    public List<QuestionsEntity> getQuestionsByTopicId(Integer topicId) {
        return questionRepository.findByTopicId(topicId);
    }

//    public List<QuestionsEntity> getRandomQuestionsByTopicId(Integer topicId) {
//        return questionRepository.findRandomQuestionsByTopicId(topicId);
//    }

    public List<QuestionsEntity> findRandomQuestionsByTopicId(int topicId, int totalQuestion) {
        return questionRepository.findRandomQuestionsByTopicId(topicId, totalQuestion);
    }

}
