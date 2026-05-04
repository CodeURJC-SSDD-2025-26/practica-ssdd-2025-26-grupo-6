package es.code.urjc.practica2.mapper;
import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.code.urjc.practica2.dto.AccountDto;
import es.code.urjc.practica2.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto toDTO(Account account);

    List<AccountDto> toDTOs(Collection<Account> accountDto);

    Account toDomain(AccountDto accountDto);
}
