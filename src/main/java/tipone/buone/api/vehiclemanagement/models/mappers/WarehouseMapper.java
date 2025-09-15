package tipone.buone.api.vehiclemanagement.models.mappers;

import org.mapstruct.Mapper;
import tipone.buone.api.vehiclemanagement.models.dto.WarehouseDTO;
import tipone.buone.api.vehiclemanagement.models.entities.Warehouse;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    WarehouseDTO toDTO(Warehouse warehouse);

    default Warehouse mapWarehouseDTOtoEntity(WarehouseDTO warehouseDTO) {
        return Warehouse.builder()
                .name(warehouseDTO.getName())
                .build();
    }
}