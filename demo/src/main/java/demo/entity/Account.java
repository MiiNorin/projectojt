package demo.entity;

import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ACCOUNT")
public class Account implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "account_id")
    private int userId;
    @Basic
    @Column(name = "role_id")
    private int roleId;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "create_date")
    private LocalDate createDate;
    @Basic
    @Column(name = "full_name")
    private String fullName;
    @Basic
    @Column(name = "birth_day")
    private LocalDate birthDay;
    @Basic
    @Column(name = "phone")
    private String phone;
    @Basic
    @Column(name = "school_name")
    private String schoolName;
    @Basic
    @Column(name = "avatar")
    private String avatar;
    @Basic
    @Column(name = "gender")
    private String gender;
    @Basic
    @Column(name = "status")
    private String status;

    public Account(int roleId, String email, String password, LocalDate createDate, String fullName, LocalDate birthDay, String phone, String schoolName, String avatar, String gender, String status) {
        this.roleId = roleId;
        this.email = email;
        this.password = password;
        this.createDate = createDate;
        this.fullName = fullName;
        this.birthDay = birthDay;
        this.phone = phone;
        this.schoolName = schoolName;
        this.avatar = avatar;
        this.gender = gender;
        this.status = status;
    }
}
