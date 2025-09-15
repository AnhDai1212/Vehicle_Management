package tipone.buone.api.vehiclemanagement.services;

import tipone.buone.api.vehiclemanagement.models.dto.BaseSearchDTO;
import tipone.buone.api.vehiclemanagement.models.dto.VehicleModelDTO;
import tipone.buone.api.vehiclemanagement.models.dto.pagination.PageListDTO;
import tipone.buone.api.vehiclemanagement.models.dto.VehicleModelUpdateDTO;
import tipone.buone.api.vehiclemanagement.models.entities.VehicleModel;

public interface VehicleModelService {
    void add(VehicleModelDTO vehicleModelDTO);
    void deleteVehicleModel(String id);
    VehicleModel findByIdAndIsActiveTrue(String vehicleModelId);
    void update(VehicleModelUpdateDTO vehicleModelUpdateDTO, String id);
    PageListDTO<VehicleModelDTO> search(BaseSearchDTO<VehicleModelDTO> request);
    VehicleModelDTO getVehicleModelDetail(String id);
}