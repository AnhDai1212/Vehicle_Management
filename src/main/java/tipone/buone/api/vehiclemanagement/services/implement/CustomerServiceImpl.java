package tipone.buone.api.vehiclemanagement.services.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tipone.buone.api.vehiclemanagement.common.constants.Constant;
import tipone.buone.api.vehiclemanagement.common.utils.Util;
import tipone.buone.api.vehiclemanagement.enums.ErrorCode;
import tipone.buone.api.vehiclemanagement.exceptions.AppException;
import tipone.buone.api.vehiclemanagement.models.dto.CustomerDTO;
import tipone.buone.api.vehiclemanagement.models.dto.CustomerUpdateDTO;
import tipone.buone.api.vehiclemanagement.models.dto.*;
import tipone.buone.api.vehiclemanagement.models.dto.pagination.PageListDTO;
import tipone.buone.api.vehiclemanagement.models.entities.Country;
import tipone.buone.api.vehiclemanagement.models.entities.Customer;
import tipone.buone.api.vehiclemanagement.models.mappers.CustomerMapper;
import tipone.buone.api.vehiclemanagement.repositories.CustomerRepository;
import tipone.buone.api.vehiclemanagement.services.CountryService;
import tipone.buone.api.vehiclemanagement.services.CustomerService;
import tipone.buone.api.vehiclemanagement.services.ImageService;
import tipone.buone.api.vehiclemanagement.specifications.CustomerSpecification;
import tipone.buone.api.vehiclemanagement.validators.ValidationFields;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CountryService countryService;
    private final CustomerMapper customerMapper;
    private final ImageService imageService;
    private final ValidationFields validationFields;

    @Override
    public void add(CustomerDTO customerDTO, MultipartFile logoFile) throws Exception {
        validationFields.validateAndThrow(customerDTO, "customerDTO", this.getClass(), "add", CustomerDTO.class, MultipartFile.class);
        if (existsByEmail(customerDTO.getEmail()))
            throw new AppException(ErrorCode.RESOURCE_ALREADY_EXISTS,"Email");
        if (existsByPhone(customerDTO.getPhone()))
            throw new AppException(ErrorCode.RESOURCE_ALREADY_EXISTS,"Phone");
        String logoPath = imageService.imagePath(logoFile);
        Country country = countryService.findByCodeAndDial(customerDTO.getCode(), customerDTO.getDial());
        Customer customer = customerMapper.mapCustomerDTOToEntity(customerDTO, country);
        customer.setActive(true);
        if (logoPath != null)
            customer.setLogoPath(logoPath);
        customerRepository.save(customer);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean existsByPhone(String phone) {
        return customerRepository.findByPhone(phone).isPresent();
    }

    @Override
    public void deleteCustomer(String id) {
        Customer customer = findByIdAndIsActiveTrue(id);
        customer.setActive(false);
        if (customer.getVehicleModels() != null) {
            customer.getVehicleModels().forEach(vm -> vm.setActive(false));
        }
        customerRepository.save(customer);
    }

    @Override
    public CustomerDTO getCustomerDetail(String id) {
        Customer customer = customerRepository.findByIdWithCountryAndIsActiveTrue(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Customer"));
        return customerMapper.toDTO(customer);
    }

    @Override
    public void update(String customerId, CustomerUpdateDTO customerUpdateDTO, MultipartFile logoFile) throws Exception {
        Customer oldCustomer = findByIdAndIsActiveTrue(customerId);
        Country oldCountry = oldCustomer.getCountry();
         if (customerUpdateDTO != null){
             validationFields.validateAndThrow(customerUpdateDTO,"customerUpdateDTO", this.getClass(),"update", String.class, CustomerUpdateDTO.class, MultipartFile.class);
             Specification<Customer> spec = CustomerSpecification.checkDuplicateFields(
                    customerId,
                    customerUpdateDTO.getEmail(),
                    customerUpdateDTO.getPhone());
            List<Customer> results = customerRepository.findAll(spec);
            for (Customer customer : results) {
                if (Objects.equals(customerUpdateDTO.getEmail(), customer.getEmail())){
                    throw new AppException(ErrorCode.RESOURCE_ALREADY_EXISTS, "Customer's email");
                }
                if (Objects.equals(customerUpdateDTO.getPhone(), customer.getPhone())){
                    throw new AppException(ErrorCode.RESOURCE_ALREADY_EXISTS, "Customer's phone number");
                }
            }
            customerMapper.updateCustomerFromDto(customerUpdateDTO, oldCustomer);
            if (customerUpdateDTO.getDial() != null || customerUpdateDTO.getCode() != null) {
                String newDial = customerUpdateDTO.getDial() != null ? customerUpdateDTO.getDial() : oldCountry.getDial();
                String newCode = customerUpdateDTO.getCode() != null ? customerUpdateDTO.getCode() : oldCountry.getCode();
                Country newCountry = countryService.findByCodeAndDial(newCode, newDial);
                oldCustomer.setCountry(newCountry);
            }
        }
        if (logoFile != null){
            imageService.deleteImage(Util.getImageIdFromUrl(oldCustomer.getLogoPath()));
            oldCustomer.setLogoPath(imageService.imagePath(logoFile));
        }
        customerRepository.save(oldCustomer);
    }

    @Override
    public Customer findByIdAndIsActiveTrue(String customerId) {
        return customerRepository.findByIdAndIsActiveTrue(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Customer"));
    }

    @Override
    public PageListDTO<CustomerDTO> search(BaseSearchDTO<CustomerDTO> request) {
        Pageable pageable = Util.toPageable(request.getSortedBy(), request.getPagination(), Constant.CUSTOMER_SORT_FIELDS);
        String keyword = Util.buildSearchKeyword(request.getSearchedBy());
        Specification<Customer> spec = Util.buildSearchSpec(
                keyword,
                Constant.CUSTOMER_SEARCH_FIELDS,
                Constant.FETCH_RELATIONS_WITH_CUSTOMER,
                "isActive"
        );
        Page<Customer> page = customerRepository.findAll(spec, pageable);
        List<CustomerDTO> rows = page.getContent().stream().map(customerMapper::toDTO).toList();
        return new PageListDTO<>(rows, (int) page.getTotalElements());
    }

    @Override
    public PageListDTO<CustomerDTO> searchCustomerSuggestionsByName(BaseSearchDTO<CustomerDTO> request) {
        Pageable pageable = Util.toPageable(request.getSortedBy(), request.getPagination(), Constant.CUSTOMER_SORT_FIELDS_FOR_MODEL);
        String keyword = Util.buildSearchKeyword(request.getSearchedBy());
        Specification<Customer> spec = Util.buildSearchSpec(
                keyword,
                Constant.CUSTOMER_SEARCH_FIELDS_FOR_MODEL,
                Constant.FETCH_RELATIONS_WITH_CUSTOMER,
                "isActive"
        );
        Page<Customer> page = customerRepository.findAll(spec, pageable);
        List<CustomerDTO> rows = page.getContent().stream().map(customerMapper::toDTO).toList();
        return new PageListDTO<>(rows, (int) page.getTotalElements());
    }
}