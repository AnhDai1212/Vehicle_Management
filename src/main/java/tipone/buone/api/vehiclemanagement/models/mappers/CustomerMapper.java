package tipone.buone.api.vehiclemanagement.models.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import tipone.buone.api.vehiclemanagement.models.dto.CustomerDTO;
import tipone.buone.api.vehiclemanagement.models.dto.CustomerUpdateDTO;
import tipone.buone.api.vehiclemanagement.models.entities.Country;
import tipone.buone.api.vehiclemanagement.models.entities.Customer;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    @Mapping(source = "country.dial", target = "dial")
    @Mapping(source = "country.code", target = "code")
    CustomerDTO toDTO(Customer customer);

    default Customer mapCustomerDTOToEntity(CustomerDTO customerDTO, Country country){
        Customer customer = Customer.builder()
                .fullName(customerDTO.getFullName())
                .phone(customerDTO.getPhone())
                .email(customerDTO.getEmail())
                .address(customerDTO.getAddress())
                .taxNumber(customerDTO.getTaxNumber())
                .build();
        if (country != null)
            customer.setCountry(country);
        return customer;
    }

    void updateCustomerFromDto(CustomerUpdateDTO customerUpdateDTO, @MappingTarget Customer entity);
}