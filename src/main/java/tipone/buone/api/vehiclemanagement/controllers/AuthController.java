package tipone.buone.api.vehiclemanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tipone.buone.api.vehiclemanagement.common.base.ApiResponse;
import tipone.buone.api.vehiclemanagement.common.constants.Constant;
import tipone.buone.api.vehiclemanagement.common.constants.MessageConstant;
import tipone.buone.api.vehiclemanagement.models.dto.auth.*;
import tipone.buone.api.vehiclemanagement.services.AccountService;
import tipone.buone.api.vehiclemanagement.services.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs to handle authentication features")
public class AuthController {
    private final AccountService accountService;
    private final AuthService authService;

    @PostMapping("/forgot-password")
    public ApiResponse forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO){
        accountService.forgotPassword(forgotPasswordDTO);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.RESEND_OTP_SUCCESS);
        return apiResponse;
    }
    @PostMapping("/verify-otp")
    public ApiResponse verifyOtp(@RequestBody OtpVerificationDTO otpVerificationDTO){
        accountService.verifyOtp(otpVerificationDTO);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.VERIFY_SUCCESS);
        return apiResponse;
    }
    @PostMapping("/reset-password")
    public ApiResponse resetPassword(@RequestBody UpdatePasswordDTO updatePasswordDTO){
        accountService.updatePassword(updatePasswordDTO);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.RESET_PASSWORD_SUCCESS);
        return apiResponse;
    }
    @PostMapping("/login")
    @Operation(summary = "Login", description = "API allows user to login using email and password")
    public ApiResponse login(@RequestBody LoginDTO loginDTO){
        AuthenticationDTO authenticationDTO = accountService.login(loginDTO);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.LOGIN_SUCCESS);
        apiResponse.setData(authenticationDTO);
        return apiResponse;
    }
    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody RegisterDTO registerDTO){
        accountService.register(registerDTO);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.REGISTER_SUCCESS);
        return apiResponse;
    }
    @PostMapping("/verify-registration")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse verifyRegistrationByOtp(@RequestBody OtpVerificationDTO otpVerificationDTO){
        accountService.verifyRegistrationByOtp(otpVerificationDTO);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setStatus(HttpStatus.CREATED.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.VERIFY_REGISTRATION_SUCCESS);
        return apiResponse;
    }
    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh Token", description = "Generate new access token using refresh token")
    public ApiResponse refreshToken(@RequestHeader(Constant.REFRESH_TOKEN) String refreshToken){
        TokenDTO tokenDTO = authService.refreshToken(refreshToken);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.TOKEN_REFRESH_SUCCESS);
        apiResponse.setData(tokenDTO);
        return apiResponse;
    }
    @PostMapping("/logout")
    public ApiResponse logout(@RequestHeader("Refresh-Token") String refreshToken){
        accountService.logout(refreshToken);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setStatus(HttpStatus.OK.getReasonPhrase());
        apiResponse.setMessage(MessageConstant.LOGOUT_SUCCESS);
        return apiResponse;
    }
}