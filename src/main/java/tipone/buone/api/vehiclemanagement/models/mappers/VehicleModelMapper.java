package tipone.buone.api.vehiclemanagement.models.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tipone.buone.api.vehiclemanagement.models.dto.VehicleModelDTO;
import tipone.buone.api.vehiclemanagement.models.entities.Customer;
import tipone.buone.api.vehiclemanagement.models.entities.VehicleModel;

@Mapper(componentModel = "spring")
public interface VehicleModelMapper {
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.fullName", target = "customerName")
    @Mapping(source = "customer.email", target = "customerEmail")
    @Mapping(source = "customer.logoPath", target = "logoPath")
    VehicleModelDTO toDTO(VehicleModel vehicleModel);

    default VehicleModel mapVehicleModelDTOToEntity(VehicleModelDTO vehicleModelDTO, Customer customer) {
        VehicleModel vehicleModel = VehicleModel.builder()
                .code(vehicleModelDTO.getCode())
                .name(vehicleModelDTO.getName())
                .type(vehicleModelDTO.getType())
                .build();
        if (customer != null)
            vehicleModel.setCustomer(customer);
        return vehicleModel;
    }
}