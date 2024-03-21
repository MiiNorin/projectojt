package demo.persistence.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TESTDETAILS", schema = "dbo", catalog = "Db_ZOTSystem")
public class TestDetails {
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
    private Date createDate;
}
