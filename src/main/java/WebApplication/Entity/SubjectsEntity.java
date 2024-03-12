package WebApplication.Entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "SUBJECTS", schema = "dbo", catalog = "Db_ZOTSystem")
public class SubjectsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "SubjectId")
    private int subjectId;
    @Basic
    @Column(name = "SubjectName")
    private String subjectName;
    @Basic
    @Column(name = "imgLink")
    private String imgLink;
    @Basic
    @Column(name = "CreateDate")
    private Date createDate;
    @Basic
    @Column(name = "Slot")
    private Integer slot;

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubjectsEntity that = (SubjectsEntity) o;

        if (subjectId != that.subjectId) return false;
        if (subjectName != null ? !subjectName.equals(that.subjectName) : that.subjectName != null) return false;
        if (imgLink != null ? !imgLink.equals(that.imgLink) : that.imgLink != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (slot != null ? !slot.equals(that.slot) : that.slot != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subjectId;
        result = 31 * result + (subjectName != null ? subjectName.hashCode() : 0);
        result = 31 * result + (imgLink != null ? imgLink.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (slot != null ? slot.hashCode() : 0);
        return result;
    }
}
