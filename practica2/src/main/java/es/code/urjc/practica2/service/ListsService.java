package es.code.urjc.practica2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.repository.ListsRepository;

@Service
public class ListsService {
    @Autowired
    private ListsRepository listsRepository;

    public Lists findById(Long id) {
        return listsRepository.findById(id).orElseThrow(() -> new RuntimeException("List not found"));
    }

    public Lists save(Lists list) {
        return listsRepository.save(list);
    }
}
