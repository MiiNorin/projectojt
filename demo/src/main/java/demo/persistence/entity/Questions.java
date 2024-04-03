package demo.persistence.entity;

import demo.repository.SubjectRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QUESTIONS", schema = "dbo", catalog = "Db_ZOTSystem")
public class Questions {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "question_id")
    private int questionId;

    @Basic
    @Column(name = "account_id")
    private Integer accountId;

    @Basic
    @Column(name = "answer_id")
    private Integer answerId;

    @Basic
    @Column(name = "image")
    private String image;

    @Basic
    @Column(name = "question_context")
    private String questionContext;

    @Basic
    @Column(name = "option_A")
    private String optionA;

    @Basic
    @Column(name = "option_B")
    private String optionB;

    @Basic
    @Column(name = "option_C")
    private String optionC;

    @Basic
    @Column(name = "option_D")
    private String optionD;

    @Basic
    @Column(name = "solution")
    private String solution;

    @Basic
    @Column(name = "status")
    private String status;
    @Basic
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @ManyToOne
    @JoinColumn(name="subject_id", nullable = false)
    private SubjectsEntity subject;
    @ManyToOne
    @JoinColumn(name="chapter_id", nullable = false)
    private ChaptersEntity chapters;
    @ManyToOne
    @JoinColumn(name="topic_id", nullable = false)
    private TopicsEntity topics;
    public String getFormattedCreateDate() {
        if (createDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return createDate.format(formatter);
        } else {
            return "";
        }
    }

}
