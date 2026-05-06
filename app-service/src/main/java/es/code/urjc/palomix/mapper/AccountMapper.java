package es.code.urjc.palomix.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.code.urjc.palomix.dto.AccountDto;
import es.code.urjc.palomix.model.Account;
import es.code.urjc.palomix.model.Lists;

// Usamos ImageMapper para convertir el avatar al ImageDto con ID
@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface AccountMapper {

    @Mapping(source = "accountAvatar", target = "avatar")
    @Mapping(source = "accountLists", target = "listIds")
    AccountDto toDTO(Account account);

    List<AccountDto> toDTOs(Collection<Account> accounts);

    @Mapping(target = "accountAvatar", ignore = true)
    @Mapping(target = "accountLists", ignore = true)
    @Mapping(target = "accountPassword", ignore = true)
    Account toDomain(AccountDto accountDto);

    default List<Long> mapListsToIds(List<Lists> accountLists) {
        if (accountLists == null) {
            return null;
        }
        return accountLists.stream()
                           .map(Lists::getListsId)
                           .collect(Collectors.toList());
    }
}