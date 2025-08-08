package libman_be.libman_be.service;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.request.LoginRequest;
import libman_be.libman_be.dto.request.RegisterRequest;
import libman_be.libman_be.dto.request.VerificationRequest;

public interface AuthService {
    public BaseResponse<String> register(RegisterRequest registerRequest);
    public BaseResponse<String> login(LoginRequest loginRequest);
    public BaseResponse<String> verifyEmail(String token);
    public BaseResponse<String> resendVerificationEmail(VerificationRequest verificationRequest);
}
