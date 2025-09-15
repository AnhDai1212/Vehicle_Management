package tipone.buone.api.vehiclemanagement.services;

public interface PasswordService {
    String encryptPassword(String password);
    boolean matches(String password, String encodedPassword);
}
