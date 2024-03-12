package WebApplication.Entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "TOPICS", schema = "dbo", catalog = "Db_ZOTSystem")
public class TopicsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "TopicId")
    private int topicId;
    @Basic
    @Column(name = "TopicName")
    private String topicName;
    @Basic
    @Column(name = "Duration")
    private String duration;
    @Basic
    @Column(name = "TotalQuestion")
    private Integer totalQuestion;
    @Basic
    @Column(name = "TopicType")
    private Integer topicType;
    @Basic
    @Column(name = "Grade")
    private Integer grade;
    @Basic
    @Column(name = "CreateDate")
    private Date createDate;
    @Basic
    @Column(name = "Status")
    private String status;
    @Basic
    @Column(name = "StartTestDate")
    private Date startTestDate;
    @Basic
    @Column(name = "FinishTestDate")
    private Date finishTestDate;
    @Basic
    @Column(name = "SubjectId")
    private Integer subjectId;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(Integer totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public Integer getTopicType() {
        return topicType;
    }

    public void setTopicType(Integer topicType) {
        this.topicType = topicType;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartTestDate() {
        return startTestDate;
    }

    public void setStartTestDate(Date startTestDate) {
        this.startTestDate = startTestDate;
    }

    public Date getFinishTestDate() {
        return finishTestDate;
    }

    public void setFinishTestDate(Date finishTestDate) {
        this.finishTestDate = finishTestDate;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopicsEntity that = (TopicsEntity) o;

        if (topicId != that.topicId) return false;
        if (topicName != null ? !topicName.equals(that.topicName) : that.topicName != null) return false;
        if (duration != null ? !duration.equals(that.duration) : that.duration != null) return false;
        if (totalQuestion != null ? !totalQuestion.equals(that.totalQuestion) : that.totalQuestion != null)
            return false;
        if (topicType != null ? !topicType.equals(that.topicType) : that.topicType != null) return false;
        if (grade != null ? !grade.equals(that.grade) : that.grade != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (startTestDate != null ? !startTestDate.equals(that.startTestDate) : that.startTestDate != null)
            return false;
        if (finishTestDate != null ? !finishTestDate.equals(that.finishTestDate) : that.finishTestDate != null)
            return false;
        if (subjectId != null ? !subjectId.equals(that.subjectId) : that.subjectId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = topicId;
        result = 31 * result + (topicName != null ? topicName.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (totalQuestion != null ? totalQuestion.hashCode() : 0);
        result = 31 * result + (topicType != null ? topicType.hashCode() : 0);
        result = 31 * result + (grade != null ? grade.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (startTestDate != null ? startTestDate.hashCode() : 0);
        result = 31 * result + (finishTestDate != null ? finishTestDate.hashCode() : 0);
        result = 31 * result + (subjectId != null ? subjectId.hashCode() : 0);
        return result;
    }
}
