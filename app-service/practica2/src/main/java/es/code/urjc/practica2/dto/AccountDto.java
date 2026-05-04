package es.code.urjc.practica2.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.code.urjc.practica2.model.Account.Role;
import es.code.urjc.practica2.model.Image;
import es.code.urjc.practica2.model.Lists;

public record AccountDto(
        Long accountId,
        String accountName,
        LocalDate accountBirthDate,
        String accountEmail,
        Role accountRole,
        String accountPassword,
        @JsonIgnore Image accountAvatar,
        String imageUrl,
        List<Lists> accountLists) {

}
