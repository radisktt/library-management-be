package libman_be.libman_be.controller;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.UserDTO;
import libman_be.libman_be.dto.response.UserResponseDTO;
import libman_be.libman_be.entity.User;
import libman_be.libman_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<BaseResponse<UserResponseDTO>> createUser(@RequestBody UserDTO userDTO) {
        UserResponseDTO userAdded =userService.createUser(userDTO);
        return ResponseEntity.ok(BaseResponse.<UserResponseDTO>builder()
                .status("success")
                .message("User created successfully")
                .data(userAdded)
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        UserResponseDTO userDto = userService.getUserById(id);
        if (userDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BaseResponse.<UserResponseDTO>builder()
                .status("success")
                .message("Get user successfully")
                .data(userDto)
                .build());
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> userDtos = userService.getAllUsers();
        return ResponseEntity.ok(BaseResponse.<List<UserResponseDTO>>builder()
                .status("success")
                .message("Get all user successfully")
                .data(userDtos)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userUpdate) {
        UserResponseDTO user = userService.updateUser(id,userUpdate);
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

}
