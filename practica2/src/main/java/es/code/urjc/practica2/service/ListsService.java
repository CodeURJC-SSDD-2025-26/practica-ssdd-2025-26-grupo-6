package es.code.urjc.practica2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.code.urjc.practica2.repository.ListsRepository;

@Service
public class ListsService {
    @Autowired
    private ListsRepository listsRepository;
}
