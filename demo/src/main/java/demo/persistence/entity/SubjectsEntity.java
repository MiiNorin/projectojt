package demo.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SUBJECTS", schema = "dbo", catalog = "Db_ZOTSystem")
public class SubjectsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "subject_id")
    private int subjectId;
    @Basic
    @Column(name = "subject_name")
    private String subjectName;
    @Basic
    @Column(name = "image")
    private String imgLink;
    @Basic
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Basic
    @Column(name = "Slot")
    private Integer slot;
    @OneToMany(mappedBy = "subjects",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChaptersEntity> chaptersEntityList;


}