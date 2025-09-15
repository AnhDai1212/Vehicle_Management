package tipone.buone.api.vehiclemanagement.services.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tipone.buone.api.vehiclemanagement.common.constants.Constant;
import tipone.buone.api.vehiclemanagement.common.utils.Util;
import tipone.buone.api.vehiclemanagement.enums.ErrorCode;
import tipone.buone.api.vehiclemanagement.enums.VehicleType;
import tipone.buone.api.vehiclemanagement.exceptions.AppException;
import tipone.buone.api.vehiclemanagement.models.dto.BaseSearchDTO;
import tipone.buone.api.vehiclemanagement.models.dto.VehicleModelDTO;
import tipone.buone.api.vehiclemanagement.models.dto.VehicleModelUpdateDTO;
import tipone.buone.api.vehiclemanagement.models.dto.pagination.PageListDTO;
import tipone.buone.api.vehiclemanagement.models.entities.Customer;
import tipone.buone.api.vehiclemanagement.models.entities.VehicleModel;
import tipone.buone.api.vehiclemanagement.models.mappers.VehicleModelMapper;
import tipone.buone.api.vehiclemanagement.repositories.VehicleModelRepository;
import tipone.buone.api.vehiclemanagement.services.CustomerService;
import tipone.buone.api.vehiclemanagement.services.VehicleModelService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleModelServiceImpl implements VehicleModelService {
    private final VehicleModelRepository vehicleModelRepository;
    private final CustomerService customerService;
    private final VehicleModelMapper vehicleModelMapper;

    @Override
    public void add(VehicleModelDTO vehicleModelDTO) {
        Customer customer = customerService.findByIdAndIsActiveTrue(vehicleModelDTO.getCustomerId());
        VehicleModel vehicleModel = vehicleModelMapper.mapVehicleModelDTOToEntity(vehicleModelDTO, customer);
        vehicleModel.setActive(true);
        vehicleModelRepository.save(vehicleModel);
    }

    @Override
    public void deleteVehicleModel(String id) {
        VehicleModel vehicleModel = findByIdAndIsActiveTrue(id);
        vehicleModel.setActive(false);
        vehicleModelRepository.save(vehicleModel);
    }

    @Override
    public VehicleModel findByIdAndIsActiveTrue(String vehicleModelId) {
        return vehicleModelRepository.findByIdAndIsActiveTrue(vehicleModelId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Vehicle model"));
    }

    @Override
    public void update(VehicleModelUpdateDTO vehicleModelUpdateDTO, String id){
        VehicleModel oldVehicleModel = findByIdAndIsActiveTrue(id);

        if (vehicleModelUpdateDTO.getType() != null)
            oldVehicleModel.setType(vehicleModelUpdateDTO.getType());
        if (vehicleModelUpdateDTO.getCustomerId() != null){
            Customer customer = customerService.findByIdAndIsActiveTrue(vehicleModelUpdateDTO.getCustomerId());
            oldVehicleModel.setCustomer(customer);
        }
        if (vehicleModelUpdateDTO.getCode() != null)
            oldVehicleModel.setCode(vehicleModelUpdateDTO.getCode());
        if (vehicleModelUpdateDTO.getName() != null)
            oldVehicleModel.setName(vehicleModelUpdateDTO.getName());

        vehicleModelRepository.save(oldVehicleModel);
    }

    @Override
    public PageListDTO<VehicleModelDTO> search(BaseSearchDTO<VehicleModelDTO> request) {
        Pageable pageable = Util.toPageable(request.getSortedBy(), request.getPagination(), Constant.VEHICLE_MODEL_SORT_FIELDS);
        String keyword = Util.buildSearchKeyword(request.getSearchedBy());
        Specification<VehicleModel> spec = Util.buildSearchSpec(
                keyword,
                Constant.VEHICLE_MODEL_SEARCH_FIELDS,
                Constant.FETCH_RELATIONS_WITH_VEHICLE_MODEL,
                "isActive"
        );
        Page<VehicleModel> page = vehicleModelRepository.findAll(spec, pageable);
        List<VehicleModelDTO> rows = page.getContent().stream().map(vehicleModelMapper::toDTO).toList();
        return new PageListDTO<>(rows, (int) page.getTotalElements());
    }

    @Override
    public VehicleModelDTO getVehicleModelDetail(String id) {
        VehicleModel vehicleModel = vehicleModelRepository.findByIdWithCustomer(id)
                .orElseThrow(()-> new AppException(ErrorCode.RESOURCE_NOT_FOUND,"Vehicle model"));
        return vehicleModelMapper.toDTO(vehicleModel);
    }
}