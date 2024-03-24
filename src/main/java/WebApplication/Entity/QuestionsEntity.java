package WebApplication.Entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "QUESTIONS", schema = "dbo", catalog = "Db_ZOTSystem")
public class QuestionsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "QuestionId")
    private int questionId;
    @Basic
    @Column(name = "SubjectId")
    private Integer subjectId;
    @Basic
    @Column(name = "AccountId")
    private Integer accountId;
    @Basic
    @Column(name = "AnswerId")
    private Integer answerId;
    @Basic
    @Column(name = "TopicId")
    private Integer topicId;
    @Basic
    @Column(name = "Image")
    private String image;
    @Basic
    @Column(name = "QuestionContext")
    private String questionContext;
    @Basic
    @Column(name = "option_a")
    private String optionA;
    @Basic
    @Column(name = "option_b")
    private String optionB;
    @Basic
    @Column(name = "option_c")
    private String optionC;
    @Basic
    @Column(name = "option_d")
    private String optionD;
    @Basic
    @Column(name = "Solution")
    private String solution;
    @Basic
    @Column(name = "Status")
    private String status;
    @Basic
    @Column(name = "CreateDate")
    private Date createDate;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuestionContext() {
        return questionContext;
    }

    public void setQuestionContext(String questionContext) {
        this.questionContext = questionContext;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionsEntity that = (QuestionsEntity) o;

        if (questionId != that.questionId) return false;
        if (subjectId != null ? !subjectId.equals(that.subjectId) : that.subjectId != null) return false;
        if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) return false;
        if (answerId != null ? !answerId.equals(that.answerId) : that.answerId != null) return false;
        if (topicId != null ? !topicId.equals(that.topicId) : that.topicId != null) return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        if (questionContext != null ? !questionContext.equals(that.questionContext) : that.questionContext != null)
            return false;
        if (optionA != null ? !optionA.equals(that.optionA) : that.optionA != null) return false;
        if (optionB != null ? !optionB.equals(that.optionB) : that.optionB != null) return false;
        if (optionC != null ? !optionC.equals(that.optionC) : that.optionC != null) return false;
        if (optionD != null ? !optionD.equals(that.optionD) : that.optionD != null) return false;
        if (solution != null ? !solution.equals(that.solution) : that.solution != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = questionId;
        result = 31 * result + (subjectId != null ? subjectId.hashCode() : 0);
        result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
        result = 31 * result + (answerId != null ? answerId.hashCode() : 0);
        result = 31 * result + (topicId != null ? topicId.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (questionContext != null ? questionContext.hashCode() : 0);
        result = 31 * result + (optionA != null ? optionA.hashCode() : 0);
        result = 31 * result + (optionB != null ? optionB.hashCode() : 0);
        result = 31 * result + (optionC != null ? optionC.hashCode() : 0);
        result = 31 * result + (optionD != null ? optionD.hashCode() : 0);
        result = 31 * result + (solution != null ? solution.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        return result;
    }
}
