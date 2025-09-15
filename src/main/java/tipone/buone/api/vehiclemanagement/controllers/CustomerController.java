package tipone.buone.api.vehiclemanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tipone.buone.api.vehiclemanagement.common.base.ApiResponse;
import tipone.buone.api.vehiclemanagement.common.constants.MessageConstant;
import tipone.buone.api.vehiclemanagement.models.dto.BaseSearchDTO;
import tipone.buone.api.vehiclemanagement.models.dto.CustomerDTO;
import tipone.buone.api.vehiclemanagement.models.dto.CustomerUpdateDTO;
import tipone.buone.api.vehiclemanagement.models.dto.pagination.PageListDTO;
import tipone.buone.api.vehiclemanagement.services.CustomerService;
import tipone.buone.api.vehiclemanagement.services.JsonService;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "APIs to handle customer's features")
public class CustomerController {
    private final CustomerService customerService;
    private final JsonService jsonService;

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Create a new customer",
            description = "Adds a new customer to the system.")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse add(@RequestPart String customerDTO, @RequestPart (required = false) MultipartFile logoFile)
            throws Exception {
        CustomerDTO dto = jsonService.parseDTO(customerDTO, CustomerDTO.class);
        customerService.add(dto, logoFile);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setStatus(HttpStatus.CREATED.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.CREATED_CUSTOMER_SUCCESS);
        return apiResponse;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.DELETED_CUSTOMER_SUCCESS);
        return apiResponse;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ApiResponse getCustomerDetail(@PathVariable String id) {
        CustomerDTO customerDTO = customerService.getCustomerDetail(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.FOUND_CUSTOMER_SUCCESS);
        apiResponse.setData(customerDTO);
        return apiResponse;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/list")
    public ApiResponse searchCustomers(@Valid @RequestBody BaseSearchDTO<CustomerDTO> request) {
        PageListDTO<CustomerDTO> list = customerService.search(request);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.CUSTOMER_LIST_SUCCESS);
        apiResponse.setData(list);
        return apiResponse;
    }

    @PatchMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Update a new customer",
            description = "Update a new customer to the system.")
    public ApiResponse update(@PathVariable String id, @RequestPart(required = false) String customerUpdateDTO,
                              @RequestPart(required = false) MultipartFile logoFile) throws Exception {
        CustomerUpdateDTO dto = (customerUpdateDTO != null)
                ? jsonService.parseDTO(customerUpdateDTO, CustomerUpdateDTO.class)
                : null;
        customerService.update(id, dto, logoFile);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.UPDATE_CUSTOMER_SUCCESS);
        return apiResponse;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/search")
    public ApiResponse searchCustomerSuggestionsByName(@Valid @RequestBody BaseSearchDTO<CustomerDTO> request) {
        PageListDTO<CustomerDTO> list = customerService.searchCustomerSuggestionsByName(request);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.CUSTOMER_SEARCH_FOR_ADD_MODEL_SUCCESS);
        apiResponse.setData(list);
        return apiResponse;
    }
}