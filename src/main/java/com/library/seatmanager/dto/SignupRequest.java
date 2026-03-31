package com.library.seatmanager.dto;

public class SignupRequest {
    private String name;
    private String phone;
    private String password;
    private String role;


    public SignupRequest(String name, String phone, String password, String role) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.role=role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public SignupRequest() {
    }
}
