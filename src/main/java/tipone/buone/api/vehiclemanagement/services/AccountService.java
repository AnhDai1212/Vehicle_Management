package tipone.buone.api.vehiclemanagement.services;

import tipone.buone.api.vehiclemanagement.enums.RoleType;
import tipone.buone.api.vehiclemanagement.models.dto.auth.*;
import tipone.buone.api.vehiclemanagement.models.entities.Account;

public interface AccountService {
    void forgotPassword(ForgotPasswordDTO resendOtpDTO);
    void verifyOtp(OtpVerificationDTO otpVerificationDTO);
    void updatePassword(UpdatePasswordDTO updatePasswordDTO);
    Account findByEmail(String email);
    AuthenticationDTO login(LoginDTO loginRequestDTO);
    void register(RegisterDTO registerDTO);
    void verifyRegistrationByOtp(OtpVerificationDTO otpVerificationDTO);
    boolean existsByEmail(String email);
    boolean existsByRole(RoleType roleType);
    void save(Account account);
    void logout(String refreshToken);
}