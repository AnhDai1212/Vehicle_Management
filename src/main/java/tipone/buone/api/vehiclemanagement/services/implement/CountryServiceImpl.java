package tipone.buone.api.vehiclemanagement.services.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tipone.buone.api.vehiclemanagement.enums.ErrorCode;
import tipone.buone.api.vehiclemanagement.exceptions.AppException;
import tipone.buone.api.vehiclemanagement.models.entities.Country;
import tipone.buone.api.vehiclemanagement.repositories.CountryRepository;
import tipone.buone.api.vehiclemanagement.services.CountryService;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public Country findByCodeAndDial(String code, String dial) {
        return countryRepository.findByDialAndCode(dial, code).orElseThrow(() -> new  AppException(ErrorCode.RESOURCE_NOT_FOUND,"Country"));
    }
}