package demo.persistence.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "ACCOUNT", schema = "dbo", catalog = "Db_ZOTSystem")
public class AccountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "account_id")
    private int accountId;
    @Basic
    @Column(name = "role_id")
    private Integer roleId;
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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return accountId == that.accountId && Objects.equals(roleId, that.roleId) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(createDate, that.createDate) && Objects.equals(fullName, that.fullName) && Objects.equals(birthDay, that.birthDay) && Objects.equals(phone, that.phone) && Objects.equals(schoolName, that.schoolName) && Objects.equals(avatar, that.avatar) && Objects.equals(gender, that.gender) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, roleId, email, password, createDate, fullName, birthDay, phone, schoolName, avatar, gender, status);
    }
}
