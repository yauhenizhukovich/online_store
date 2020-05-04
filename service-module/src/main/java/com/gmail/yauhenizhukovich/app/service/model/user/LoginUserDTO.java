package com.gmail.yauhenizhukovich.app.service.model.user;

public class LoginUserDTO {

    private String email;
    private String password;
    private RoleEnumService role;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setRole(RoleEnumService role) {
        this.role = role;
    }

    public RoleEnumService getRole() {
        return role;
    }

}
