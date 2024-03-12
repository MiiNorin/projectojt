package WebApplication.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ANSWERS", schema = "dbo", catalog = "Db_ZOTSystem")
public class AnswersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "AnswerId")
    private int answerId;
    @Basic
    @Column(name = "Answer")
    private String answer;

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnswersEntity that = (AnswersEntity) o;

        if (answerId != that.answerId) return false;
        if (answer != null ? !answer.equals(that.answer) : that.answer != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = answerId;
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        return result;
    }
}
