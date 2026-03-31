package es.code.urjc.practica2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Director {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long directorId;

    private String directorName;
    private String directorBirthDate;

    public Director() {} //Default constructor for JPA
    
    public Director(String directorName, String birthDate) {
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
