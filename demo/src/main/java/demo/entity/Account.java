package demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private Date createDate;
    @Basic
    @Column(name = "full_name")
    private String fullName;
    @Basic
    @Column(name = "birth_day")
    private Date birthDay;
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



}
