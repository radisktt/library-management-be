package libman_be.libman_be.mapper;

import libman_be.libman_be.dto.response.UserResponseDTO;
import libman_be.libman_be.entity.Role;
import libman_be.libman_be.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponseDTO toDTO(User user) {
        if (user == null) return null;

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setAvatar(user.getAvatar());
        dto.setGender(user.getGender() != null ? user.getGender().toString(): "null");
        // Chuyển Set<Role> thành Set<String>
        Set<String> roleNames = mapRolesToString(user.getRoles());
        dto.setRoles(roleNames);

        return dto;
    }

    public List<UserResponseDTO> toDtoList(List<User> users) {
        if (users == null) return null;
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private Set<String> mapRolesToString(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}
