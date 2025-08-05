package libman_be.libman_be.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String phoneNumber;
    private String address;
    private boolean isActive;
    private Set<String> role;
}
