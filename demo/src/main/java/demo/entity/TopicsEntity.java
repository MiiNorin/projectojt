package demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;
    private String topicName;
    private int duration;
    private int totalQuestion;
    private String topicType;
    private String grade;
    private String createDate;
    private String status;
    private String startTestDate;
    private String finishTestDate;
    private Long subjectId;
    private Long chapterId;
}