package tipone.buone.api.vehiclemanagement.enums;

public enum RoleType {
    ADMIN("admin"),
    USER("user");

    private final String value;

    RoleType(String value) {
        this.value = value;
    }
}