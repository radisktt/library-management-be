package libman_be.libman_be.service;

import libman_be.libman_be.dto.UserDTO;
import libman_be.libman_be.dto.request.RegisterRequest;
import libman_be.libman_be.dto.response.UserResponseDTO;
import libman_be.libman_be.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDTO createUser(UserDTO user);
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUser(Long id, UserDTO user);
    ResponseEntity<String> deleteUserById(Long id);


    String uploadAvatar(Long id, MultipartFile file) throws IOException;
}
