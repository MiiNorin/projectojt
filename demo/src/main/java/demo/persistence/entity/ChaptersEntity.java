package demo.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CHAPTERS", schema = "dbo", catalog = "Db_ZOTSystem")
public class ChaptersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "chapter_id")
    private int chapterId;
    @ManyToOne
    @JoinColumn(name="subject_id", nullable = false)
    private SubjectsEntity subjects;
    @Basic
    @Column(name = "chapter_name")
    private String chapterName;
    @OneToMany(mappedBy = "chapter",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TopicsEntity> topicsEntityList;
}
