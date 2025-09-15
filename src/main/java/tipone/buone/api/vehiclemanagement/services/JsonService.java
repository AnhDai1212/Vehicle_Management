package tipone.buone.api.vehiclemanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface JsonService {
    <T> T parseDTO(String rawJson, Class<T> targetType) throws JsonProcessingException;
}