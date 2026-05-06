package es.code.urjc.palomix.model;

import java.time.LocalDate;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

//It's the user account, but to avoid later problems we call it account
@Entity
public class Account {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long accountId;

    private String accountName;
    private LocalDate accountBirthDate;
    private String accountEmail;
    private Role accountRole;
    private String accountPassword;

    public enum Role {
        USER,
        ADMIN
    }

    @OneToMany(mappedBy = "listOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lists> accountLists = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "accountAvatar_id")
    private Image accountAvatar;

    public Account() {} //Default constructor for JPA

    public Account(String accountName, LocalDate accountBirthDate, String accountEmail, Role accountRole, String accountPassword) {
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

    public LocalDate getAccountBirthDate() {
        return accountBirthDate;
    }

    public void setAccountBirthDate(LocalDate accountBirthDate) {
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

    public List<Lists> getAccountLists() { 
        return accountLists;
    
    }

    public void setAccountLists(List<Lists> accountLists) {
        this.accountLists = accountLists; 
    }
    public Image getAccountAvatar() { 
        return accountAvatar;
    
    }
    public void setAccountAvatar(Image accountAvatar) {
        this.accountAvatar = accountAvatar; 
    }
}
