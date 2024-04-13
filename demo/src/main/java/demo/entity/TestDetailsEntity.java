package demo.entity;


import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "TESTDETAILS", schema = "dbo", catalog = "Db_ZOTSystem")
public class TestDetailsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "test_detail_id")
    private int testDetailId;
    @Basic
    @Column(name = "account_id")
    private Integer accountId;
    @Basic
    @Column(name = "score")
    private Double score;
    @Basic
    @Column(name = "submitted")
    private Boolean submitted;
    @Basic
    @Column(name = "create_date")
    private LocalDateTime createDate;

    public int getTestDetailId() {
        return testDetailId;
    }

    public void setTestDetailId(int testDetailId) {
        this.testDetailId = testDetailId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Boolean getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Boolean submitted) {
        this.submitted = submitted;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestDetailsEntity that = (TestDetailsEntity) o;

        if (testDetailId != that.testDetailId) return false;
        if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) return false;
        if (score != null ? !score.equals(that.score) : that.score != null) return false;
        if (submitted != null ? !submitted.equals(that.submitted) : that.submitted != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = testDetailId;
        result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (submitted != null ? submitted.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        return result;
    }
}