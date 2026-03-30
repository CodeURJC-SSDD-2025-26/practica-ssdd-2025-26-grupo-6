package es.code.urjc.practica2.model;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//It's the user account, but to avoid later problems we call it account
@Entity
public class Account {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long accountId;

    private String accountName;
    private String accountBirthDate;
    private String accountEmail;
    private Role accountRole;
    private String accountPassword;

    public enum Role {
        USER,
        ADMIN
    }

    public Account() {} //Default constructor for JPA

    public Account(Long accountId, String accountName, String accountBirthDate, String accountEmail, Role accountRole, String accountPassword) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountBirthDate = accountBirthDate;
        this.accountEmail = accountEmail;
        this.accountRole = accountRole;
        this.accountPassword = accountPassword;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountBirthDate() {
        return accountBirthDate;
    }

    public void setAccountBirthDate(String accountBirthDate) {
        this.accountBirthDate = accountBirthDate;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public Role getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(Role accountRole) {
        this.accountRole = accountRole;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }
}
