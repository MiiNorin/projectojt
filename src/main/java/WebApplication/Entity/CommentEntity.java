package WebApplication.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "COMMENT", schema = "dbo", catalog = "Db_ZOTSystem")
public class CommentEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "comment_id")
    private int commentId;
    @Basic
    @Column(name = "account_id")
    private Integer accountId;
    @Basic
    @Column(name = "test_id")
    private Integer testId;
    @Basic
    @Column(name = "commentcontext")
    private String commentcontext;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getCommentcontext() {
        return commentcontext;
    }

    public void setCommentcontext(String commentcontext) {
        this.commentcontext = commentcontext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentEntity that = (CommentEntity) o;

        if (commentId != that.commentId) return false;
        if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) return false;
        if (testId != null ? !testId.equals(that.testId) : that.testId != null) return false;
        if (commentcontext != null ? !commentcontext.equals(that.commentcontext) : that.commentcontext != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = commentId;
        result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
        result = 31 * result + (testId != null ? testId.hashCode() : 0);
        result = 31 * result + (commentcontext != null ? commentcontext.hashCode() : 0);
        return result;
    }
}
