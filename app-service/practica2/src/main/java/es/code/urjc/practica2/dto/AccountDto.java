package es.code.urjc.practica2.dto;

import java.time.LocalDate;
import java.util.List; 

import es.code.urjc.practica2.model.Account.Role;


public record AccountDto(
        Long accountId,
        String accountName,
        LocalDate accountBirthDate,
        String accountEmail,
        Role accountRole,
        ImageDto avatar,
        List<Long> listIds) { //Just IDs to avoid recursion
}
