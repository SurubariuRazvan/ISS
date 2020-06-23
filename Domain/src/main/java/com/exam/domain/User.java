package com.exam.domain;

import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
@Table(name = "User_table", uniqueConstraints = {@UniqueConstraint(name = "unique_username", columnNames = {"username"})})
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements com.exam.domain.Entity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "user_type")
    private UserType userType;

    public User() {
    }

    public User(Integer id, String username, String password, String name, UserType userType) {
        this.id = id;
        this.username = username;
        this.password = User.encodePassword(password);
        this.name = name;
        this.userType = userType;
    }

    public User(User user) {
        this.id = user.id;
        this.username = user.username;
        this.password = user.password;
        this.name = user.name;
        this.userType = user.userType;
    }

    public static String encodePassword(String password) {
        return DigestUtils.sha256Hex(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", userType=" + userType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
