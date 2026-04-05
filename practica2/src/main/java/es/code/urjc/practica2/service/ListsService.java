package es.code.urjc.practica2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.repository.ListsRepository;

@Service
public class ListsService {
    @Autowired
    private ListsRepository listsRepository;

    public List<Lists> findByOwner(Account owner) {
        return listsRepository.findByListOwner(owner);
    }

    public void save(Lists list) {
        listsRepository.save(list);
    }

    public void delete(Long id) {
        listsRepository.deleteById(id);
    }
}
