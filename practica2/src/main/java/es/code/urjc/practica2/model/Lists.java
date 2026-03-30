package es.code.urjc.practica2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.List;
import java.util.ArrayList;

@Entity
public class Lists {
    @Id
    private Long listsId;

    private String listName;

    @ManyToMany
    private List<Filmography> filmographyList = new ArrayList<>();

    public Lists() {} //Default constructor for JPA

    public Lists(Long listsId, String listName, List<Filmography> filmographyList) {
        this.listsId = listsId;
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
