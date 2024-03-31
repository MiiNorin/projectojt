package WebApplication.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "QUESTIONTESTS", schema = "dbo", catalog = "Db_ZOTSystem")
public class QuestiontestsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "TestId")
    private int testId;
    @Basic
    @Column(name = "testdetail_id")
    private Integer testDetailId;
    @Basic
    @Column(name = "QuestionId")
    private Integer questionId;
    @Basic
    @Column(name = "AnswerId")
    private Integer answerId;

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public Integer getTestDetailId() {
        return testDetailId;
    }

    public void setTestDetailId(Integer testDetailId) {
        this.testDetailId = testDetailId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestiontestsEntity that = (QuestiontestsEntity) o;

        if (testId != that.testId) return false;
        if (testDetailId != null ? !testDetailId.equals(that.testDetailId) : that.testDetailId != null) return false;
        if (questionId != null ? !questionId.equals(that.questionId) : that.questionId != null) return false;
        if (answerId != null ? !answerId.equals(that.answerId) : that.answerId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = testId;
        result = 31 * result + (testDetailId != null ? testDetailId.hashCode() : 0);
        result = 31 * result + (questionId != null ? questionId.hashCode() : 0);
        result = 31 * result + (answerId != null ? answerId.hashCode() : 0);
        return result;
    }
}
