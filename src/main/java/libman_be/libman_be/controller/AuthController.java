package libman_be.libman_be.controller;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.request.LoginRequest;
import libman_be.libman_be.dto.request.RegisterRequest;
import libman_be.libman_be.dto.response.UserResponseDTO;
import libman_be.libman_be.entity.Role;
import libman_be.libman_be.entity.User;
import libman_be.libman_be.exception.UserException;
import libman_be.libman_be.mapper.UserMapper;
import libman_be.libman_be.repository.RoleRepository;
import libman_be.libman_be.repository.UserRepository;
import libman_be.libman_be.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public BaseResponse<String> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException.UserAlreadyExistsException(request.getEmail());
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Confirm password does not match");
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
//        UserResponseDTO responseDTO = userMapper.toDTO(savedUser);

        return BaseResponse.<String>builder()
                .status("sucess")
                .message("Account created successfully")
                .data("Email " + savedUser.getEmail() +" is created")
                .build();
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

        String token = jwtUtils.generateToken(user.getEmail());

        return BaseResponse.<String>builder()
                .status("sucess")
                .message(user.getEmail() + " is logged in")
                .data(token)
                .build();
    }

}
