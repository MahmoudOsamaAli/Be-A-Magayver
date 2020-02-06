package com.example.beamagayver.pojo;

public class Admin {
    private String code;
    private String email;
    private String name;

    public Admin(){
        //required empty constructor
         }
    public Admin(String code, String email) {
        this.code = code;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
