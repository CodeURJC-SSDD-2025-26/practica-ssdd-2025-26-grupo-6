package es.code.urjc.practica2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import java.util.List;
import java.util.ArrayList;

@Entity
public class Lists {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long listsId;

    private String listName;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account listOwner;

    @ManyToMany
    @JoinTable(
        name = "list_filmographies",
        joinColumns = @JoinColumn(name = "list_id"),
        inverseJoinColumns = @JoinColumn(name = "filmography_id")
    )
    private List<Filmography> filmographyList = new ArrayList<>();

    public Lists() {} //Default constructor for JPA

    public Lists(String listName, List<Filmography> filmographyList) {
        this.listName = listName;
        this.filmographyList = filmographyList;
    }

    public Long getListsId() {
        return listsId;
    }

    public void setListsId(Long listsId) {
        this.listsId = listsId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public List<Filmography> getFilmographyList() {
        return filmographyList;
    }

    public void setFilmographyList(List<Filmography> filmographyList) {
        this.filmographyList = filmographyList;
    }
}
