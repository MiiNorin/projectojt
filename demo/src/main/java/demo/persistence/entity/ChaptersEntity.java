package demo.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CHAPTERS", schema = "dbo", catalog = "Db_ZOTSystem")
public class ChaptersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "chapter_id")
    private int chapterId;

    @ManyToOne
    @JoinColumn(name="subject_id", nullable = false, referencedColumnName = "subject_id")
    private SubjectsEntity subjects;
    @Basic
    @Column(name = "subject_id")
    private Integer subjectId;
    @Basic
    @Column(name = "chapter_name")
    private String chapterName;

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }
}
