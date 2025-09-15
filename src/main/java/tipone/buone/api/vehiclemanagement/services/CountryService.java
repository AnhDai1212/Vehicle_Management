package tipone.buone.api.vehiclemanagement.services;

import tipone.buone.api.vehiclemanagement.models.entities.Country;

public interface CountryService {
    Country findByCodeAndDial(String code, String dial);
}