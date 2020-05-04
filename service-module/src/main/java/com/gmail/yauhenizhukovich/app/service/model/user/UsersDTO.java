package com.gmail.yauhenizhukovich.app.service.model.user;

public class UsersDTO {

    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private RoleEnumService role;
    private String uniqueNumber;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setRole(RoleEnumService role) {
        this.role = role;
    }

    public RoleEnumService getRole() {
        return role;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

}
