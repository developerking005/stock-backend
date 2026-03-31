package com.library.seatmanager.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String phone;

    private String password;

    private Double walletBalance;

    private LocalDateTime createdAt;

    private String role;

    public User(Long id, Double walletBalance, LocalDateTime createdAt, String password, String phone, String name, String role) {
        this.id = id;
        this.walletBalance = walletBalance;
        this.createdAt = createdAt;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.role=role;
    }

    /* ADD THIS */
    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {

        private final User user;

        public Builder(){
            user = new User();
        }

        public Builder name(String name){
            user.name = name;
            return this;
        }

        public Builder phone(String phone){
            user.phone = phone;
            return this;
        }

        public Builder password(String password){
            user.password = password;
            return this;
        }

        public Builder wallet(Double wallet){
            user.walletBalance = wallet;
            return this;
        }

        public Builder createdAt(LocalDateTime time){
            user.createdAt = time;
            return this;
        }

        public Builder role(String role){
            user.role = role;
            return this;
        }

        public User build(){

            if(user.phone == null)
                throw new RuntimeException("Phone required");

            if(user.password == null)
                throw new RuntimeException("Password required");

            return user;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", walletBalance=" + walletBalance +
                ", createdAt=" + createdAt +
                ", role=" + role +
                '}';
    }
}
