package tipone.buone.api.vehiclemanagement.enums;

import lombok.Getter;

@Getter
public enum VehicleType {
    TRUCK("Truck"),
    SUV("SUV"),
    PASSENGERS_CAR("Passengers car"),
    SEDAN("Sedan");
    
    private final String displayName;
    
    VehicleType(String displayName) {
        this.displayName = displayName;
    }
}