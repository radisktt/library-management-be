package libman_be.libman_be.service.impl;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.request.LoginRequest;
import libman_be.libman_be.dto.request.RegisterRequest;
import libman_be.libman_be.dto.request.VerificationRequest;
import libman_be.libman_be.entity.Role;
import libman_be.libman_be.entity.User;
import libman_be.libman_be.event.UserRegistrationEvent;
import libman_be.libman_be.exception.AuthException;
import libman_be.libman_be.exception.TokenException;
import libman_be.libman_be.exception.UserException;
import libman_be.libman_be.repository.RoleRepository;
import libman_be.libman_be.repository.UserRepository;
import libman_be.libman_be.service.AuthService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final RedisTemplate<String, String> redisTemplate;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, ApplicationEventPublisher applicationEventPublisher,
                           RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public BaseResponse<String> register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException.UserAlreadyExistsException(request.getEmail() +" already exists");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AuthException.InvalidCredentialsException("Confirm password does not match");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(false);
        user.setGender(null);
        user.setPhoneNumber(null);
        user.setAvatar(null);
        user.setAddress(null);

        Role userRole = roleRepository.findByName(Role.RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Default role USER not found"));

        user.setRoles(Collections.singleton(userRole));

        User savedUser = userRepository.save(user);
        //UserResponseDTO responseDTO = userMapper.toDTO(savedUser);

        applicationEventPublisher.publishEvent(new UserRegistrationEvent(this, savedUser));
        return BaseResponse.<String>builder()
                .status("sucess")
                .message("Account created successfully")
                .data("Email " + savedUser.getEmail() +" is created")
                .build();
    }

    @Override
    public BaseResponse<String> login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public BaseResponse<String> verifyEmail(String token) {
        String email = redisTemplate.opsForValue().get("verification:" + token);
        if (email == null) {
            throw new TokenException.InvalidTokenException("Invalid or expired verification token");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException.UserNotFoundException("User with email " + email + " not found"));
        user.setActive(true);
        userRepository.save(user);
        redisTemplate.delete("verification:" + token);
        return BaseResponse.<String>builder()
                .status("success")
                .message("Email verification successful")
                .data("Email verified successfully")
                .build();
    }

    @Override
    public BaseResponse<String> resendVerificationEmail(VerificationRequest verificationRequest) {
        String email = verificationRequest.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException.UserNotFoundException("User with email " + email + " not found"));
        if(!passwordEncoder.matches(verificationRequest.getPassword(), user.getPassword())) {
            throw new AuthException.InvalidCredentialsException("Invalid password");
        }

        applicationEventPublisher.publishEvent(new UserRegistrationEvent(this, user));

        return BaseResponse.<String>builder()
                .status("success")
                .message("Resend mail verification successful")
                .data("Mail verification sent successfully")
                .build();
    }

}
