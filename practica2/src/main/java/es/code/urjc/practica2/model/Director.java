package es.code.urjc.practica2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Director {
    @Id
    private Long directorId;

    private String directorName;
    private String directorBirthDate;

    public Director() {} //Default constructor for JPA
    
    public Director(Long directorId, String directorName, String birthDate) {
        this.directorId = directorId;
        this.directorName = directorName;
        this.directorBirthDate = birthDate;
    }

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getDirectorBirthDate() {
        return directorBirthDate;
    }

    public void setDirectorBirthDate(String directorBirthDate) {
        this.directorBirthDate = directorBirthDate;
    }
}
