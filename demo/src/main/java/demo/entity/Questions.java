package demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QUESTIONS")
public class  Questions {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "question_id")
    private int questionId;
    @Basic
    @Column(name = "subject_id")
    private Integer subjectId;
    @Basic
    @Column(name = "account_id")
    private Integer accountId;
    @Basic
    @Column(name = "answer_id")
    private Integer answerId;
    @Basic
    @Column(name = "topic_id")
    private Integer topicId;
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
    private Date createDate;


}
