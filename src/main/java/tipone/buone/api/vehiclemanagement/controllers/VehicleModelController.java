package tipone.buone.api.vehiclemanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tipone.buone.api.vehiclemanagement.common.base.ApiResponse;
import tipone.buone.api.vehiclemanagement.common.constants.MessageConstant;
import tipone.buone.api.vehiclemanagement.models.dto.BaseSearchDTO;
import tipone.buone.api.vehiclemanagement.models.dto.VehicleModelDTO;
import tipone.buone.api.vehiclemanagement.models.dto.pagination.PageListDTO;
import tipone.buone.api.vehiclemanagement.models.dto.VehicleModelUpdateDTO;
import tipone.buone.api.vehiclemanagement.services.VehicleModelService;

@RestController
@RequestMapping("/vehicle-models")
@RequiredArgsConstructor
public class VehicleModelController {
    private final VehicleModelService vehicleModelService;

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ApiResponse add(@Valid @RequestBody VehicleModelDTO vehicleModelDTO){
        vehicleModelService.add(vehicleModelDTO);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setStatus(HttpStatus.CREATED.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.CREATED_VEHICLE_MODEL_SUCCESS);
        return apiResponse;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse deleteVehicleModel(@PathVariable String id) {
        vehicleModelService.deleteVehicleModel(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.DELETED_VEHICLE_MODEL_SUCCESS);
        return apiResponse;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update a vehicle model",
            description = "Update a vehicle to the system.")
    public ApiResponse update(@PathVariable String id, @Valid @RequestBody VehicleModelUpdateDTO vehicleModelUpdateDTO){
        vehicleModelService.update(vehicleModelUpdateDTO, id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setMessage(MessageConstant.UPDATE_VEHICLE_MODEL_SUCCESS);
        return apiResponse;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/list")
    public ApiResponse searchVehicleModels(@Valid @RequestBody BaseSearchDTO<VehicleModelDTO> request) {
        PageListDTO<VehicleModelDTO> list = vehicleModelService.search(request);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.VEHICLE_MODEL_LIST_SUCCESS);
        apiResponse.setData(list);
        return apiResponse;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ApiResponse getDetail(@PathVariable String id) {
        VehicleModelDTO vehicleModelDTO = vehicleModelService.getVehicleModelDetail(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.FOUND_VEHICLE_MODEL_SUCCESS);
        apiResponse.setData(vehicleModelDTO);
        return apiResponse;
    }
}