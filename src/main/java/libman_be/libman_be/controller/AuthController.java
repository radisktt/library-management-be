package libman_be.libman_be.controller;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.request.LoginRequest;
import libman_be.libman_be.dto.request.RegisterRequest;
import libman_be.libman_be.dto.request.VerificationRequest;
import libman_be.libman_be.dto.response.UserResponseDTO;
import libman_be.libman_be.entity.Role;
import libman_be.libman_be.entity.User;
import libman_be.libman_be.exception.UserException;
import libman_be.libman_be.mapper.UserMapper;
import libman_be.libman_be.repository.RoleRepository;
import libman_be.libman_be.repository.UserRepository;
import libman_be.libman_be.service.AuthService;
import libman_be.libman_be.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<String>> register(@RequestBody RegisterRequest request) {
        BaseResponse<String> response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public BaseResponse<?> login(@RequestBody LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            throw new UserException.UserNotFoundException("Invalid email or password");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException.UserNotFoundException("Invalid email or password");
        }

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().toString())
                .collect(Collectors.toList());
        String token = jwtUtils.generateToken(user.getEmail(),roles);

        return BaseResponse.<String>builder()
                .status("sucess")
                .message(user.getEmail() + " is logged in")
                .data(token)
                .build();
    }

    @GetMapping("/verify")
    public ResponseEntity<BaseResponse<String>> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<BaseResponse<String>> resendVerificationEmail(@RequestBody VerificationRequest request) {
        return ResponseEntity.ok(authService.resendVerificationEmail(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<String>> logout(@RequestHeader("Authorization") String authHeader) {
        if(authHeader!=null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            redisTemplate.opsForValue().set(
                    "blacklist:"+token, "true",
                    Duration.ofMillis(jwtUtils.extractExpiration(token).getTime()- System.currentTimeMillis())
            );
            return ResponseEntity.ok(BaseResponse.<String>builder()
                    .status("success")
                    .message("Logged out successfully")
                    .data(null)
                    .build());
        }
        return ResponseEntity.badRequest().body(BaseResponse.<String>builder()
                .status("error")
                .message("Invalid or missing token")
                .data(null)
                .build());
    }
}
