package tipone.buone.api.vehiclemanagement.services.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tipone.buone.api.vehiclemanagement.enums.CacheDuration;
import tipone.buone.api.vehiclemanagement.enums.ErrorCode;
import tipone.buone.api.vehiclemanagement.enums.RoleType;
import tipone.buone.api.vehiclemanagement.exceptions.AppException;
import tipone.buone.api.vehiclemanagement.models.dto.auth.*;
import tipone.buone.api.vehiclemanagement.models.entities.Account;
import tipone.buone.api.vehiclemanagement.models.entities.RefreshToken;
import tipone.buone.api.vehiclemanagement.models.entities.Role;
import tipone.buone.api.vehiclemanagement.models.mappers.AccountMapper;
import tipone.buone.api.vehiclemanagement.repositories.AccountRepository;
import tipone.buone.api.vehiclemanagement.services.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class  AccountServiceImpl implements AccountService {
    private final EmailService emailService;
    private final OtpService otpService;
    private final PasswordService passwordService;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AccountMapper accountMapper;
    private final RoleService roleService;
    private final CacheService cacheService;

    @Override
    public void forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        String email = forgotPasswordDTO.getEmail();
        findByEmail(forgotPasswordDTO.getEmail());
        if (!otpService.isOtpLimitExceeded(email, CacheDuration.CACHE_RESEND_OTP_REQUEST.getCacheName())){
            otpService.deleteOtp(email, CacheDuration.CACHE_OTP.getCacheName());
            throw new AppException(ErrorCode.OTP_LIMIT_EXCEEDED, "the daily Resend");
        }
        otpService.increaseRequestCount(email, CacheDuration.CACHE_RESEND_OTP_REQUEST.getCacheName());
        emailService.sendOtpByEmail(email);
    }

    @Override
    public void verifyOtp(OtpVerificationDTO otpVerificationDTO) {
        String cachedOtp = otpService.getOtp(otpVerificationDTO.getEmail());
        if (cachedOtp != null && otpService.verifyOtp(otpVerificationDTO.getOtp(), cachedOtp)){
            otpService.deleteOtp(otpVerificationDTO.getEmail(), CacheDuration.CACHE_OTP.getCacheName());
            cacheService.put(CacheDuration.CACHE_VERIFIED_EMAILS.getCacheName(), otpVerificationDTO.getEmail(), true);
        }
        else throw new AppException(ErrorCode.INVALID_OTP, "");
    }

    @Override
    public void updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        if (!Boolean.TRUE.equals(cacheService.fetch(CacheDuration.CACHE_VERIFIED_EMAILS.getCacheName(), updatePasswordDTO.getEmail(), Boolean.class)))
            throw new AppException(ErrorCode.OTP_NOT_VERIFIED);
        Account account = findByEmail(updatePasswordDTO.getEmail());
        String password = passwordService.encryptPassword(updatePasswordDTO.getNewPassword());
        account.setPassword(password);
        accountRepository.save(account);
        cacheService.delete(CacheDuration.CACHE_VERIFIED_EMAILS.getCacheName(), updatePasswordDTO.getEmail());
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND,"Account"));
    }

    @Override
    public AuthenticationDTO login(LoginDTO loginRequestDTO) {
        Account account = accountRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));
        if(!passwordService.matches(loginRequestDTO.getPassword(), account.getPassword())){
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
        if(!account.isActive()){
            throw new AppException(ErrorCode.ACCOUNT_LOCKED);
        }
        Set<String> roleNames = account.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
        String accessToken = jwtService.generateToken(account.getId(), account.getEmail(), roleNames);
        String refreshToken = refreshTokenService.generateRefreshToken(account);
        return AuthenticationDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .account(accountMapper.toDTO(account))
                .build();
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        String email = registerDTO.getEmail();
        if (!otpService.isOtpLimitExceeded(email, CacheDuration.CACHE_RESEND_OTP_REQUEST_FOR_REGISTER.getCacheName())) {
            otpService.deleteOtp(email, CacheDuration.CACHE_OTP.getCacheName());
            throw new AppException(ErrorCode.OTP_LIMIT_EXCEEDED, "send");
        }
        RegisterDTO cachedRegistration = cacheService.fetch(
                CacheDuration.CACHE_REGISTRATION.getCacheName(),
                registerDTO.getEmail(),
                RegisterDTO.class
        );
        if (cachedRegistration != null) {
            String hashedPassword = passwordService.encryptPassword(registerDTO.getPassword());
            if (!cachedRegistration.getPassword().equals(hashedPassword)) {
                cachedRegistration.setPassword(hashedPassword);
                cacheService.put(CacheDuration.CACHE_REGISTRATION.getCacheName(), email, cachedRegistration);
            }
            emailService.sendOtpByEmail(email);
            otpService.increaseRequestCount(email, CacheDuration.CACHE_RESEND_OTP_REQUEST_FOR_REGISTER.getCacheName());
            return;
        }
        if (accountRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.RESOURCE_ALREADY_EXISTS, "Email");
        }
        String hashedPassword = passwordService.encryptPassword(registerDTO.getPassword());
        RegisterDTO newCachedRegistration = new RegisterDTO();
        newCachedRegistration.setEmail(email);
        newCachedRegistration.setPassword(hashedPassword);
        cacheService.put(CacheDuration.CACHE_REGISTRATION.getCacheName(), email, newCachedRegistration);
        emailService.sendOtpByEmail(email);
        otpService.increaseRequestCount(email, CacheDuration.CACHE_RESEND_OTP_REQUEST_FOR_REGISTER.getCacheName());
    }

    @Override
    public void verifyRegistrationByOtp(OtpVerificationDTO otpVerificationDTO) {
        RegisterDTO cachedRegistration = cacheService.fetch(
                CacheDuration.CACHE_REGISTRATION.getCacheName(),
                otpVerificationDTO.getEmail(),
                RegisterDTO.class
        );
        if (cachedRegistration == null) {
            otpService.deleteOtp(otpVerificationDTO.getEmail(), CacheDuration.CACHE_OTP.getCacheName());
            throw new AppException(ErrorCode.INVALID_OTP, "Register's");
        }
        String cachedOtp = otpService.getOtp(otpVerificationDTO.getEmail());
        if (cachedOtp == null || !otpService.verifyOtp(otpVerificationDTO.getOtp(), cachedOtp)) {
            throw new AppException(ErrorCode.INVALID_OTP, "Register's");
        }
        Role userRole = roleService.getByType(RoleType.USER).orElse(null);
        Account account = accountMapper.mapRegisterDtoToEntity(cachedRegistration, userRole);
        accountRepository.save(account);
        cacheService.delete(CacheDuration.CACHE_REGISTRATION.getCacheName(), otpVerificationDTO.getEmail());
        cacheService.delete(CacheDuration.CACHE_RESEND_OTP_REQUEST_FOR_REGISTER.getCacheName(), otpVerificationDTO.getEmail());
        otpService.deleteOtp(otpVerificationDTO.getEmail(), CacheDuration.CACHE_OTP.getCacheName());
    }

    @Override
    public void logout(String refreshToken){
        RefreshToken rf = refreshTokenService.findByRefreshToken(refreshToken);
        rf.setRevoked(true);
        rf.setRevokedAt(LocalDateTime.now());
        refreshTokenService.save(rf);
    }

    @Override
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByRole(RoleType roleType) {
        return accountRepository.existsByRoles_Name(roleType);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }
}