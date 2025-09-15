package tipone.buone.api.vehiclemanagement.services;

import org.springframework.web.multipart.MultipartFile;
import tipone.buone.api.vehiclemanagement.models.dto.BaseSearchDTO;
import tipone.buone.api.vehiclemanagement.models.dto.CustomerDTO;
import tipone.buone.api.vehiclemanagement.models.dto.pagination.PageListDTO;
import tipone.buone.api.vehiclemanagement.models.dto.CustomerUpdateDTO;
import tipone.buone.api.vehiclemanagement.models.entities.Customer;

public interface CustomerService {
    void add(CustomerDTO customerDTO, MultipartFile logoFile) throws Exception;
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    void deleteCustomer(String id);
    CustomerDTO getCustomerDetail(String id);
    void update(String customerId, CustomerUpdateDTO customerUpdateDTO, MultipartFile logoFile) throws Exception;
    Customer findByIdAndIsActiveTrue(String customerId);
    PageListDTO<CustomerDTO> search(BaseSearchDTO<CustomerDTO> request);
    PageListDTO<CustomerDTO> searchCustomerSuggestionsByName(BaseSearchDTO<CustomerDTO> request);
}