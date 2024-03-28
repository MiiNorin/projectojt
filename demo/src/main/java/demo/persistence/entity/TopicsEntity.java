package demo.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TOPICS", schema = "dbo", catalog = "Db_ZOTSystem")
public class TopicsEntity {

    @OneToMany(mappedBy = "topicsEntity", cascade = CascadeType.ALL)
    private List<Questions> questionList;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "topic_id")
    private int topicId;

    @Basic
    @Column(name = "topic_name")
    private String topicName;

    @Basic
    @Column(name = "Duration")
    private String duration;

    @Basic
    @Column(name = "total_question")
    private int totalQuestion; // Thay đổi kiểu dữ liệu

    @Basic
    @Column(name = "topic_type")
    private Integer topicType;

    @Basic
    @Column(name = "Grade")
    private Integer grade;

    @Basic
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "start_test_date")
    private LocalDateTime startTestDate;

    @Basic
    @Column(name = "finish_test_date")
    private LocalDateTime finishTestDate;

    @Basic
    @Column(name = "subject_id")
    private Integer subjectId;

    // hashCode và equals có thể sử dụng @Data hoặc lombok để tự động sinh
}
