package libman_be.libman_be.dto.response;

import libman_be.libman_be.entity.Role;
import libman_be.libman_be.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String gender;
    private String phoneNumber;
    private String address;
    private String avatar;
    private boolean isActive;
    private Set<String> roles;

}