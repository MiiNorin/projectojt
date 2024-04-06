package demo.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TOPICS", schema = "dbo", catalog = "Db_ZOTSystem")
public class TopicsEntity {

    @ManyToOne
    @JoinColumn(name="chapter_id", nullable = false)
    private ChaptersEntity chapter;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "topic_id")
    private int topicId;
    @OneToMany(mappedBy = "topics",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Questions> questions;

    @Basic
    @Column(name = "topic_name")
    private String topicName;

    @Basic
    @Column(name = "Duration")
    private int duration;

    @Basic
    @Column(name = "total_question")
    private int totalQuestion;

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
    public String getFormattedCreateDate() {
        if (createDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return createDate.format(formatter);
        } else {
            return "";
        }
    }
    public String getFormattedStartDate() {
        if (startTestDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return startTestDate.format(formatter);
        } else {
            return "";
        }
    }
    public String getFormattedFinishDate() {
        if (finishTestDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return finishTestDate.format(formatter);
        } else {
            return "";
        }
    }


}
