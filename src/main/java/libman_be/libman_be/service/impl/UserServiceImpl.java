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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO createUser(UserDTO user) {
        User addUser = new User();
        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        addUser.setName(user.getName());
        addUser.setEmail(user.getEmail());
        addUser.setPassword(passwordEncoder.encode(user.getPassword()));
        addUser.setActive(false);
        addUser.setGender(User.Gender.valueOf(user.getGender()));
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
                throw new RuntimeException("Email already exists");
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


}
