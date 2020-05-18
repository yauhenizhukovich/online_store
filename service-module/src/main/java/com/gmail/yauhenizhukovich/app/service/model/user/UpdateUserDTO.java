package com.gmail.yauhenizhukovich.app.service.model.user;

public class UpdateUserDTO {

    private boolean updatePassword;
    private RoleEnumService role;
    private String uniqueNumber;

    public boolean isUpdatePassword() {
        return updatePassword;
    }

    public void setUpdatePassword(boolean updatePassword) {
        this.updatePassword = updatePassword;
    }

    public RoleEnumService getRole() {
        return role;
    }

    public void setRole(RoleEnumService role) {
        this.role = role;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

}
