package demo.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TESTDETAILS", schema = "dbo", catalog = "Db_ZOTSystem")
public class TestDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_detail_id")
    private int testDetailid;

    @ManyToOne
    @JoinColumn(name="account_id", nullable = false)
    private Account accounts;

    @Basic
    @Column(name = "score")
    private Double score;

    @Basic
    @Column(name = "submitted")
    private Boolean submitted;

    @Basic
    @Column(name = "create_date")
    private Date createDate;

    // Thêm trường topicId
    @Basic
    @Column(name = "topic_id")
    private Integer topicId;


}