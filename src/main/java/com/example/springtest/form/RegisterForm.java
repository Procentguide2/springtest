package com.example.springtest.form;

import java.util.regex.Pattern;

public class RegisterForm {

    private String username;
    private String password;


    public RegisterForm() {
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
}
