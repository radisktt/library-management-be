package libman_be.libman_be.service.impl;

import libman_be.libman_be.dto.UserDTO;
import libman_be.libman_be.dto.request.RegisterRequest;
import libman_be.libman_be.dto.response.UserResponseDTO;
import libman_be.libman_be.entity.Role;
import libman_be.libman_be.entity.User;
import libman_be.libman_be.exception.UserException;
import libman_be.libman_be.mapper.UserMapper;
import libman_be.libman_be.repository.RoleRepository;
import libman_be.libman_be.repository.UserRepository;
import libman_be.libman_be.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO createUser(UserDTO user) {
        if(userRepository.existsByEmail(user.getEmail())){
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new UserException.UserAlreadyExistsException(user.getEmail());
            }
        }
        User addUser = new User();
        addUser.setName(user.getName());
        addUser.setEmail(user.getEmail());
        addUser.setPassword(passwordEncoder.encode(user.getPassword()));
        addUser.setActive(false);
        if (user.getGender() != null) {
            try {
                addUser.setGender(User.Gender.valueOf(user.getGender().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid gender value: " + user.getGender());
            }
        }
        addUser.setAddress(user.getAddress());
        addUser.setPhoneNumber(user.getPhoneNumber());
        addUser.setActive(false);
        if(user.getRole() == null){
            Role defaultRole = roleRepository.findByName(Role.RoleName.USER)
                    .orElseThrow(() -> new RuntimeException("No default role found"));
            addUser.setRoles((Set.of(defaultRole)));
        }
        userRepository.save(addUser);
        return userMapper.toDTO(addUser);
    }


    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException.UserNotFoundException("User with id " + id + " not found"));
        return userMapper.toDTO(user);
    }
    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserDTO userUpdate) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException.UserNotFoundException("User with id " + id + " not found"));
        if(userUpdate.getEmail() != null && !userUpdate.getEmail().equals(user.getEmail())){
            if(userRepository.existsByEmail(userUpdate.getEmail())){
                if (userRepository.existsByEmail(user.getEmail())) {
                    throw new UserException.UserAlreadyExistsException(user.getEmail());
                }
            }
        }
        if (userUpdate.getName() != null) {
            user.setName(userUpdate.getName());
        }

        if (userUpdate.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        }

        if (userUpdate.getRole() != null && !userUpdate.getRole().isEmpty()) {
            Set<Role> roleEntities = new HashSet<>();
            for (String roleName : userUpdate.getRole()) {
                Role.RoleName enumRoleName;
                try {
                    enumRoleName = Role.RoleName.valueOf(roleName);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid role name: " + roleName);
                }
                Role role = roleRepository.findByName(enumRoleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                roleEntities.add(role);
            }
            user.setRoles(roleEntities);
        }

        if (userUpdate.getGender() != null) {
            try {
                user.setGender(User.Gender.valueOf(userUpdate.getGender()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid gender value: " + userUpdate.getGender());
            }
        }

        if (userUpdate.getPhoneNumber() != null) {
            user.setPhoneNumber(userUpdate.getPhoneNumber());
        }
        user.setAddress(userUpdate.getAddress());
        user.setActive(userUpdate.isActive());

        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }


    @Override
    public ResponseEntity<String> deleteUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
        }

        userRepository.deleteById(id);

        // Kiểm tra chắc chắn đã xóa
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
        }
    }

    @Override
    public String uploadAvatar(Long userId, MultipartFile file) throws IOException {
        log.debug("Processing avatar upload for user ID: {}", userId);

        // Kiểm tra user tồn tại
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException.UserNotFoundException("User not found with ID: " + userId));

        // Kiểm tra file
        if (file.isEmpty()) {
            log.warn("Empty file uploaded for user ID: {}", userId);
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        // Kiểm tra định dạng file (chỉ cho phép ảnh)
        String contentType = file.getContentType();
        if (!contentType.startsWith("image/")) {
            log.warn("Invalid file type uploaded for user ID: {}, contentType: {}", userId, contentType);
            throw new IllegalArgumentException("Only image files are allowed (jpg, png, etc.)");
        }

        // Tạo thư mục nếu chưa tồn tại
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            log.debug("Creating upload directory: {}", uploadDir);
            directory.mkdirs();
        }

        // Tạo tên file duy nhất
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = "user_" + userId + "_" + UUID.randomUUID() + extension;
        Path filePath = Paths.get(uploadDir, newFilename);

        // Lưu file vào thư mục
        log.debug("Saving avatar file to: {}", filePath);
        Files.write(filePath, file.getBytes());

        // Cập nhật đường dẫn avatar vào user
        String avatarPath = "/uploads/avatars/" + newFilename;
        user.setAvatar(avatarPath);
        userRepository.save(user);
        log.debug("Updated avatar path for user ID: {} to {}", userId, avatarPath);

        return avatarPath;
    }


}
