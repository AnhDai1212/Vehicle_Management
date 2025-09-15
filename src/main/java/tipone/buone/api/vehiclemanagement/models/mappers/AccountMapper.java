package tipone.buone.api.vehiclemanagement.models.mappers;

import org.mapstruct.Mapper;
import tipone.buone.api.vehiclemanagement.models.dto.AccountDTO;
import tipone.buone.api.vehiclemanagement.models.dto.auth.RegisterDTO;
import tipone.buone.api.vehiclemanagement.models.entities.Account;
import tipone.buone.api.vehiclemanagement.models.entities.Role;

import java.util.Set;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface AccountMapper {
    AccountDTO toDTO(Account account);

    default Account mapRegisterDtoToEntity(RegisterDTO registerDTO, Role role) {
        Account account = new Account();
        account.setEmail(registerDTO.getEmail());
        account.setPassword(registerDTO.getPassword());
        account.setActive(true);
        if (role != null) {
            account.setRoles(Set.of(role));
        }
        return account;
    }
}